package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.universal.BadGatewayException;
import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.universal.GatewayTimeoutException;
import com.stargazerstudios.existence.conductor.erratum.universal.UserNotFoundException;
import com.stargazerstudios.existence.conductor.utils.JwtUtil;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.entity.Role;
import com.stargazerstudios.existence.symphony.repository.RoleDAO;
import com.stargazerstudios.existence.symphony.repository.UserDAO;
import com.stargazerstudios.existence.symphony.entity.User;
import com.stargazerstudios.existence.symphony.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private WebClient webClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO login(UserWrapper wUser)
            throws UserNotFoundException, BadGatewayException, GatewayTimeoutException, EntityNotFoundException {
        HashMap<String, String> parsedJSON = wUser.getUser();
        String username = parsedJSON.get("username");
        String password = parsedJSON.get("password");
        Optional<User> userData = userDAO.findByUsername(username);
        // If user is found, check if raw password matches with hash
        if (userData.isPresent()) {
            User user = userData.get();
            //if password matches build a token
            if (passwordEncoder.matches(password, user.getPassword())) {
                user.setPassword(password);
                String token = generateToken(user);
                user.setToken(token);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                return new UserDTO(user);
            } else {
                throw new UserNotFoundException();
            }
        } else {
            if (authViaLDAP(username, password)) {
                User user = createUser(username, password);
                user.setPassword(password);
                String token = generateToken(user);
                user.setToken(token);
                return new UserDTO(user);
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    @Override
    public UserDTO autologin(String token) throws UserNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(auth);
        String _token = generateTokenFromAuth(auth);

        Optional<User> userData = userDAO.findByUsername(auth.getName());
        if (userData.isPresent()) {
            User user = userData.get();
            user.setPassword(null);
            user.setToken(_token);
            return new UserDTO(user);
        } else {
            throw new UserNotFoundException();
        }
    }

    private String generateToken(User user) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
                  user.getUsername(),
                  user.getPassword()
          )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtil.generateToken(authentication);
    }

    private String generateTokenFromAuth(Authentication authentication) {
        return jwtUtil.generateToken(authentication);
    }

    private boolean authViaLDAP(String username, String password) throws UserNotFoundException, GatewayTimeoutException {
        Mono<User> userMono = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/login")
                        .queryParam("username", username)
                        .queryParam("password", password)
                        .build())
                .retrieve()
                .bodyToMono(User.class)
                .timeout(Duration.ofMillis(20000))
                .onErrorReturn(new User());

        try {
            User user = userMono.block(Duration.ofMillis(20000));
            if (user != null) {
                if (user.getUsername().equals(username)) {
                    return true;
                } else {
                    throw new UserNotFoundException();
                }
            } else {
                throw new BadGatewayException();
            }
        } catch (RuntimeException | BadGatewayException e) {
            throw new GatewayTimeoutException();
        }
    }

    private User createUser(String username, String password) throws EntityNotFoundException {
        Optional<User> userData = userDAO.findByUsername(username);

        if (userData.isPresent()) {
            User user = userData.get();
            String hashPword = passwordEncoder.encode(password);
            user.setPassword(hashPword);
            return userDAO.save(user);
        }

        String hashPword = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(hashPword);
        Optional<Role> roleData = roleDAO.findByName("ROLE_USER");
        if (roleData.isPresent()) {
            Role role = roleData.get();
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(role);
            user.setRoles(roleSet);
        } else {
            throw new EntityNotFoundException("There is an error in the roles database. Please contact system admin.");
        }

        return userDAO.save(user);
    }
}
