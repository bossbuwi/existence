package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.constants.EnumUtilOutput;
import com.stargazerstudios.existence.conductor.erratum.universal.*;
import com.stargazerstudios.existence.conductor.utils.AuthorityUtil;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.entity.Role;
import com.stargazerstudios.existence.symphony.entity.User;
import com.stargazerstudios.existence.symphony.repository.RoleDAO;
import com.stargazerstudios.existence.symphony.repository.UserDAO;
import com.stargazerstudios.existence.symphony.utils.UserUtil;
import com.stargazerstudios.existence.symphony.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserAccessServiceImpl implements UserAccessService{

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private AuthorityUtil authorityUtil;

    @Autowired
    private StringUtil stringUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userDAO.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user: users) {
            UserDTO userDTO = userUtil.wrapUser(user);
            userDTOs.add(userDTO);
        }
        return userDTOs;
    }

    @Override
    public UserDTO getUser(UserWrapper user)
            throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException {
        return null;
    }

    @Override
    public UserDTO createUser(UserWrapper wUser)
            throws DuplicateEntityException, InvalidInputException, UserUnauthorizedException, DatabaseErrorException, EntityNotFoundException {
        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.SUPERUSER.getValue());
        if (!isAuthorized) throw new UserUnauthorizedException();

        String username = stringUtil.checkInputTrim(wUser.getUsername());
        if (username.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("username");

        String password = stringUtil.checkInputTrim(wUser.getPassword());
        if (password.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("password");

        User user = new User();
        user.setUsername(username);
        String hashPword = passwordEncoder.encode(password);
        user.setPassword(hashPword);

        Optional<Role> userRole = roleDAO.findByName(EnumAuthorization.USER.getValue());
        if (userRole.isEmpty()) throw new EntityNotFoundException("There is an error in the Roles database. Please contact system admin.");
        Role role = userRole.get();
        Set<Role> roles = Collections.singleton(role);
        user.setRoles(roles);

        try {
            userDAO.save(user);
        } catch (DataIntegrityViolationException e) {
            // TODO: Handle the error correctly.
            //  It is recommended to create a new error class that will return the message from the DataIntegrityViolation
            String message = e.getRootCause().getMessage();
            e.getCause().printStackTrace();
            throw new DatabaseErrorException(message);
        }

        return userUtil.wrapUser(user);
    }

    @Override
    public UserDTO updateUserPassword(UserWrapper wUser)
            throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException, DatabaseErrorException {
        String username = stringUtil.checkInputTrim(wUser.getUsername());
        if (username.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("username");

        String password = stringUtil.checkInputTrim(wUser.getPassword());
        if (password.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("password");

        String authUsername = authorityUtil.getAuthUsername();
        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.SUPERUSER.getValue());

        if (!authUsername.equals(username) && !isAuthorized) throw new UserUnauthorizedException();

        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isEmpty()) throw new EntityNotFoundException("User with username: " + username + " not found.");
        User user = userData.get();
        String hashPword = passwordEncoder.encode(password);
        user.setPassword(hashPword);
        return userUtil.wrapUser(userDAO.save(user));
    }

    @Override
    public UserDTO deleteUser(UserWrapper user)
            throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException {
        return null;
    }

    @Override
    public UserDTO addRoles(UserWrapper wUser)
            throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException {
        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.SUPERUSER.getValue());
        if (!isAuthorized) throw new UserUnauthorizedException();

        String username = stringUtil.checkInputTrim(wUser.getUsername());
        if (username.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("username");

        String authUsername = authorityUtil.getAuthUsername();
        if (username.equals(authUsername)) throw new UserUnauthorizedException();

        String[] rolesIn = wUser.getRoles();
        if (rolesIn == null || rolesIn.length == 0) throw new InvalidInputException("roles");

        Set<String> rolesQuery = new HashSet<>();
        for (String role: rolesIn) {
            if (!stringUtil.checkInputTrim(role).equals(EnumUtilOutput.EMPTY.getValue())) {
                rolesQuery.add(role);
            }
        }

        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isEmpty()) throw new EntityNotFoundException("User with username: " + username + " not found.");

        List<Role> rolesDb = roleDAO.findRolesBySet(rolesQuery);
        if (rolesDb.size() != rolesQuery.size()) throw new InvalidCollectionException("roles");

        User user = userData.get();
        Set<Role> userRoles = user.getRoles();
        Set<Role> addRoles = new HashSet<>(rolesDb);
        userRoles.addAll(addRoles);

        user.setRoles(userRoles);
        return userUtil.wrapUser(userDAO.save(user));
    }

    @Override
    public UserDTO removeRoles(UserWrapper wUser)
            throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException {
        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.SUPERUSER.getValue());
        if (!isAuthorized) throw new UserUnauthorizedException();

        String username = stringUtil.checkInputTrim(wUser.getUsername());
        if (username.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("username");

        String authUsername = authorityUtil.getAuthUsername();
        if (username.equals(authUsername)) throw new UserUnauthorizedException();

        String[] rolesIn = wUser.getRoles();
        if (rolesIn == null || rolesIn.length == 0) throw new InvalidInputException("roles");

        Set<String> rolesQuery = new HashSet<>();
        for (String role: rolesIn) {
            if (!stringUtil.checkInputTrim(role).equals(EnumUtilOutput.EMPTY.getValue())) {
                rolesQuery.add(role);
            }
        }

        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isEmpty()) throw new EntityNotFoundException("User with username: " + username + " not found.");

        List<Role> rolesDb = roleDAO.findRolesBySet(rolesQuery);
        if (rolesDb.size() != rolesQuery.size()) throw new InvalidCollectionException("roles");

        User user = userData.get();
        Set<Role> userRoles = user.getRoles();
        Set<Role> removeRoles = new HashSet<>(rolesDb);
        userRoles.removeAll(removeRoles);

        user.setRoles(userRoles);
        return userUtil.wrapUser(userDAO.save(user));
    }

    @Override
    public UserDTO banUser(UserWrapper user)
            throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException {
        return null;
    }

    @Override
    public UserDTO unbanUser(UserWrapper user)
            throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException {
        return null;
    }
}
