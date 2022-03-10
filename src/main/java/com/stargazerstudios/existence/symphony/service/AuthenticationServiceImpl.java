package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.constants.EnumUtilOutput;
import com.stargazerstudios.existence.conductor.erratum.universal.*;
import com.stargazerstudios.existence.conductor.utils.JwtUtil;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.entity.Role;
import com.stargazerstudios.existence.symphony.repository.RoleDAO;
import com.stargazerstudios.existence.symphony.repository.UserDAO;
import com.stargazerstudios.existence.symphony.entity.User;
import com.stargazerstudios.existence.symphony.utils.RoleUtil;
import com.stargazerstudios.existence.symphony.utils.UserUtil;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;

@Service
@Transactional
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
    private StringUtil stringUtil;

    @Autowired
    private RoleUtil roleUtil;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private WebClient webClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO login(UserWrapper wUser)
            throws UserNotFoundException, BadGatewayException, GatewayTimeoutException, EntityNotFoundException, InvalidInputException, AppUninitializedErrorException {
        // This is a super messy method
        // TODO: Needs a rework for efficiency and scalability
        String username = stringUtil.checkInput(wUser.getUsername());
        String password = stringUtil.checkInput(wUser.getPassword());
        if (username.equals(EnumUtilOutput.EMPTY.getValue()) || password.equals(EnumUtilOutput.EMPTY.getValue()))
            throw new InvalidInputException("username or password");
        Optional<User> userData = userDAO.findByUsername(username);
        // If user is found, check if raw password matches with hash
        if (userData.isPresent()) {
            User dbUser = userData.get();

            // These codes prevent the method from directly referencing or modifying the Optional<User> object
            // This prevent other Optional<User> calls on the same transaction from being affected
            // TODO: Find a more elegant way to do this.
            //  These codes are messy and could easily become the source of unforeseen bugs.
            //  Also, these codes need to be repeated on other methods called during login that utilizes the Optional<User> call.
            User user = new User();
            user.setId(dbUser.getId());
            user.setUsername(dbUser.getUsername());
            user.setPassword(dbUser.getPassword());
            user.setRoles(dbUser.getRoles());

            //if password matches build a token
            if (passwordEncoder.matches(password, user.getPassword())) {
                user.setPassword(password);
                String token = generateToken(user);
                user.setToken(token);
                // This line doesn't do anything and only for debugging purposes
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                return userUtil.wrapUser(user);
            } else {
                throw new UserNotFoundException();
            }
        } else if (username.equals(EnumAuthorization.DEFAULT_USER.getValue())){
            throw new AppUninitializedErrorException();
        } else {
            // TODO: This needs to be checked and reworked.
            //  The process of authenticating through third party is messy.
            if (authViaLDAP(username, password)) {
                User user = createUser(username, password);
                user.setPassword(password);
                String token = generateToken(user);
                user.setToken(token);
                return userUtil.wrapUser(user);
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
            return userUtil.wrapUser(user);
        } else {
            throw new UserNotFoundException();
        }
    }

    private String generateToken(User user) throws AuthenticationException {
        // Password passed here must be the plain password
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
        Optional<Role> roleData = roleDAO.findByName(EnumAuthorization.USER.getValue());
        if (roleData.isPresent()) {
            Role role = roleData.get();
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(role);
            user.setRoles(roleSet);
        } else {
            throw new EntityNotFoundException("There is an error in the Roles database. Please contact system admin.");
        }

        return userDAO.save(user);
    }
}
