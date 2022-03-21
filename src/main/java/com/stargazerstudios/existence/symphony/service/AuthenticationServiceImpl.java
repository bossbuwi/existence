package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.constants.EnumUtilOutput;
import com.stargazerstudios.existence.conductor.erratum.authorization.UserUnauthorizedException;
import com.stargazerstudios.existence.conductor.erratum.database.EntitySaveErrorException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.input.InvalidInputException;
import com.stargazerstudios.existence.conductor.erratum.root.*;
import com.stargazerstudios.existence.conductor.erratum.system.FatalErrorException;
import com.stargazerstudios.existence.conductor.erratum.system.InvalidPropertyErrorException;
import com.stargazerstudios.existence.conductor.erratum.thirdparty.GatewayErrorException;
import com.stargazerstudios.existence.conductor.utils.AuthorityUtil;
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
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.*;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    @Value("${jwt.secret}")
    private String secret;

    @Value("${ldap.server}")
    private String ldapServer;

    @Value("${ldap.uri}")
    private String ldapUri;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthorityUtil authorityUtil;

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
            throws UnknownInputException, AuthorizationErrorException,
                SystemErrorException, ThirdPartyErrorException,
                DatabaseErrorException, EntityErrorException {
        String username = stringUtil.checkInput(wUser.getUsername());
        String password = stringUtil.checkInput(wUser.getPassword());
        if (username.equals(EnumUtilOutput.EMPTY.getValue()) || password.equals(EnumUtilOutput.EMPTY.getValue()))
            throw new InvalidInputException("username or password");
        Optional<User> userData = userDAO.findByUsername(username);
        // If user is found, check if raw password matches with hash
        if (userData.isPresent()) {
            User user = userData.get();
            // check if username is banned
            boolean isBanned = authorityUtil.isBanned(user.getRoles());
            if (isBanned) throw new UserUnauthorizedException();
            //if password matches build a token
            if (passwordEncoder.matches(password, user.getPassword())) {
                user.setPassword(password);
                String token = generateToken(user);
                user.setToken(token);
                // This line doesn't do anything and only for debugging purposes
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                return userUtil.wrapUser(user);
            } else {
                boolean isAuth = isAuthViaLDAP(username, password);
                if (isAuth) {
                    updateThirdPartyPassword(username, password);
                    user.setPassword(password);
                    String token = generateToken(user);
                    user.setToken(token);
                    return userUtil.wrapUser(user);
                } else {
                    throw new UserUnauthorizedException();
                }
            }
        } else if (username.equals(EnumAuthorization.DEFAULT_USER.getValue())){
            throw new FatalErrorException();
        } else {
            boolean isAuth = isAuthViaLDAP(username,password);
            if (!isAuth) throw new UserUnauthorizedException();
            createUser(username, password);

            Optional<User> newUserData = userDAO.findByUsername(username);
            if (newUserData.isEmpty()) throw new FatalErrorException();

            User newUser = newUserData.get();
            newUser.setPassword(password);
            String token = generateToken(newUser);
            newUser.setToken(token);

            return userUtil.wrapUser(newUser);
        }
    }

    @Override
    public UserDTO autologin(String token) throws AuthorizationErrorException {
        // TODO: Check if this method even works
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
            throw new UserUnauthorizedException();
        }
    }

    @Override
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        // TODO: Funnily enough this technically doesn't really do anything.
        //  It does log out the user from the app, security context, cookies and all
        //  but the JSON web token associated with the user's session is not invalidated.
        //  As such, if the user tries to access any unguarded endpoint with the JSON web token on its
        //  HTTP headers, the user will automatically get logged in, making logging out useless.
        //  A process to invalidate JSON web tokens must be developed in order to log the user out permanently.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CookieClearingLogoutHandler cookieClearingLogoutHandler =
                new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        cookieClearingLogoutHandler.logout(request, response, auth);
        securityContextLogoutHandler.logout(request, response, auth);
        return true;
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

    private boolean isAuthViaLDAP(String username, String password)
            throws SystemErrorException, AuthorizationErrorException, ThirdPartyErrorException {
        User loginDetails = new User();
        loginDetails.setUsername(username);
        loginDetails.setPassword(password);

        if (stringUtil.checkInput(ldapServer).equals(EnumUtilOutput.EMPTY.getValue()))
            throw new InvalidPropertyErrorException("ldap.server");

        Mono<User> userMono = webClient.post()
                .uri(ldapUri)
                .body(Mono.just(loginDetails), User.class)
                .retrieve()
                .bodyToMono(User.class);

        try {
            User userLDAP = userMono.block(Duration.ofMillis(20000));
            return userLDAP.getUsername() != null && !userLDAP.getUsername().isEmpty();
        } catch (UnsupportedMediaTypeException e) {
            throw new UserUnauthorizedException();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GatewayErrorException();
        }
    }

    private User createUser(String username, String password)
            throws EntityErrorException, DatabaseErrorException {
        String hashPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(hashPassword);
        Optional<Role> roleData = roleDAO.findByName(EnumAuthorization.USER.getValue());
        if (roleData.isPresent()) {
            Role role = roleData.get();
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(role);
            user.setRoles(roleSet);
        } else {
            throw new EntityNotFoundException("role", "name", EnumAuthorization.USER.getValue());
        }

        try {
            userDAO.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("user");
        }
        return user;
    }

    private User updateThirdPartyPassword(String username, String password)
            throws DatabaseErrorException, AuthorizationErrorException{
        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isEmpty()) throw new UserUnauthorizedException();

        User user = userData.get();
        String hashPassword = passwordEncoder.encode(password);
        user.setPassword(hashPassword);

        try {
            userDAO.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("user");
        }
        return user;
    }
}
