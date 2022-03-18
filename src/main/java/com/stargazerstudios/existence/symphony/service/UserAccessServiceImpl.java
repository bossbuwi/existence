package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.constants.EnumRank;
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

import java.util.*;

@Service
public class UserAccessServiceImpl implements UserAccessService{
    // TODO: Add process to allow superusers to reset and give a new password to lesser users
    //  or other superusers without knowing their old passwords

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
    public UserDTO getUser(UserWrapper wUser)
            throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException {
        String username = stringUtil.checkInputTrim(wUser.getUsername());
        if (username.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("username");

        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isEmpty()) throw new InvalidInputException("username");

        User user = userData.get();
        return userUtil.wrapUser(user);
    }

    @Override
    public UserDTO createUser(UserWrapper wUser)
            throws DuplicateEntityException, InvalidInputException, UserUnauthorizedException,
                DatabaseErrorException, EntityNotFoundException {
        long authRank = authorityUtil.getHighestRank();
        if (authRank > EnumRank.ROLE_SUPERUSER.getValue()) throw new UserUnauthorizedException();

        String username = stringUtil.checkInputTrim(wUser.getUsername());
        if (username.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("username");

        String password = stringUtil.checkInputTrim(wUser.getPassword());
        if (password.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("password");

        User user = new User();
        user.setUsername(username);
        String hashPword = passwordEncoder.encode(password);
        user.setPassword(hashPword);

        Optional<Role> userRole = roleDAO.findByName(EnumAuthorization.USER.getValue());
        if (userRole.isEmpty()) throw new EntityNotFoundException("role", "name", EnumAuthorization.USER.getValue());
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

        String oldPassword = stringUtil.checkInputTrim(wUser.getOld_password());
        if (oldPassword.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("old_password");

        String newPassword = stringUtil.checkInputTrim(wUser.getNew_password());
        if (newPassword.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("new_password");

        String confirmPassword = stringUtil.checkInputTrim(wUser.getConfirm_password());
        if (confirmPassword.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("confirm_password");
        if (!newPassword.equals(confirmPassword)) throw new InvalidInputException("confirm_password");

        String authUsername = authorityUtil.getAuthUsername();
        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.SUPERUSER.getValue());

        if (!authUsername.equals(username) && !isAuthorized) throw new UserUnauthorizedException();

        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isEmpty()) throw new EntityNotFoundException("user", "username", username);
        User user = userData.get();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) throw new InvalidInputException("old_password");
        if (oldPassword.equals(newPassword)) throw new InvalidInputException("new_password");
        String hashPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashPassword);

        try {
            userDAO.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseErrorException("The is a problem saving the changes. Please contact an admin.");
        }

        return userUtil.wrapUser(user);
    }

    @Override
    public UserDTO deleteUser(UserWrapper wUser)
            throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException, FatalErrorException {
        boolean isSuperUserOrHigher = authorityUtil.checkAuthority(EnumAuthorization.SUPERUSER.getValue());
        if (!isSuperUserOrHigher) throw new UserUnauthorizedException();

        String username = stringUtil.checkInputTrim(wUser.getUsername());
        if (username.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("username");

        String authUsername = authorityUtil.getAuthUsername();
        if (username.equals(authUsername)) throw new UserUnauthorizedException();

        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isEmpty()) throw new EntityNotFoundException("user", "username", username);

        User user = userData.get();
        long userRank = authorityUtil.getHighestRank(user);
        long authRank = authorityUtil.getHighestRank();
        if (authorityUtil.hasHigherRank(userRank, authRank)) throw new UserUnauthorizedException();

        try {
            userDAO.delete(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FatalErrorException();
        }
        return userUtil.wrapUser(user);
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

        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isEmpty()) throw new EntityNotFoundException("user", "username", username);
        User user = userData.get();

        if (authorityUtil.isBanned(user.getRoles())) throw new InvalidInputException("username");

        long userRank = authorityUtil.getHighestRank(user);
        long authRank = authorityUtil.getHighestRank();
        if (authorityUtil.hasHigherRank(userRank, authRank)) throw new UserUnauthorizedException();

        Set<String> rolesQuery = new HashSet<>();
        for (String role: rolesIn) {
            if (!stringUtil.checkInputTrim(role).equals(EnumUtilOutput.EMPTY.getValue())) {
                rolesQuery.add(role);
            }
        }

        List<Role> rolesDb = roleDAO.findRolesBySet(rolesQuery);
        if (rolesDb.size() != rolesQuery.size()) throw new InvalidCollectionException("roles");

        long inputRank = authorityUtil.getHighestRank(rolesDb);
        if (authorityUtil.hasHigherRank(inputRank, authRank)) throw new UserUnauthorizedException();

        Set<Role> userRoles = user.getRoles();
        Set<Role> addRoles = new HashSet<>(rolesDb);
        userRoles.addAll(addRoles);

        user.setRoles(userRoles);
        return userUtil.wrapUser(userDAO.save(user));
    }

    @Override
    public UserDTO removeRoles(UserWrapper wUser)
            throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException,
                DatabaseErrorException, GenericErrorException {
        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.SUPERUSER.getValue());
        if (!isAuthorized) throw new UserUnauthorizedException();

        String username = stringUtil.checkInputTrim(wUser.getUsername());
        if (username.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("username");

        String authUsername = authorityUtil.getAuthUsername();
        if (username.equals(authUsername)) throw new UserUnauthorizedException();

        String[] rolesIn = wUser.getRoles();
        if (rolesIn == null || rolesIn.length == 0) throw new InvalidInputException("roles");

        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isEmpty()) throw new EntityNotFoundException("user", "username", username);
        User user = userData.get();

        if (authorityUtil.isBanned(user.getRoles())) throw new InvalidInputException("username");

        long userRank = authorityUtil.getHighestRank(user);
        long authRank = authorityUtil.getHighestRank();
        if (authorityUtil.hasHigherRank(userRank, authRank)) throw new UserUnauthorizedException();

        Set<String> rolesQuery = new HashSet<>();
        for (String role: rolesIn) {
            if (!stringUtil.checkInputTrim(role).equals(EnumUtilOutput.EMPTY.getValue())) {
                rolesQuery.add(role);
            }
        }

        Set<Role> userRoles = user.getRoles();

        Set<Role> removeRoles = new HashSet<>();
        List<Role> userRoleList = new ArrayList<>(userRoles);
        for (String roleName: rolesQuery) {
            Role result = userRoleList.stream()
                    .filter(role ->
                        roleName.equals(role.getName())
                    )
                    .findAny()
                    .orElse(null);
            if (result != null) {
                removeRoles.add(result);
            } else {
                throw new  GenericErrorException("User does not have the role: " + roleName + ".");
            }
        }

        userRoles.removeAll(removeRoles);
        if (userRoles.size() == 1) throw new GenericErrorException("User should have at least one role.");

        user.setRoles(userRoles);

        try {
            userDAO.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("user");
        }

        return userUtil.wrapUser(user);
    }

    @Override
    public UserDTO banUser(UserWrapper wUser)
            throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException, FatalErrorException {
        String username = stringUtil.checkInputTrim(wUser.getUsername());
        if (username.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("username");

        String authUsername = authorityUtil.getAuthUsername();
        if (username.equals(authUsername)) throw new UserUnauthorizedException();

        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isEmpty()) throw new EntityNotFoundException("user", "username", username);
        User user = userData.get();

        boolean isBanned = authorityUtil.isBanned(user.getRoles());
        if (isBanned) throw new InvalidInputException("username"); // TODO: This must be more specific.

        Optional<Role> banData = roleDAO.findByName(EnumAuthorization.BANNED.getValue());
        if (banData.isEmpty()) throw new FatalErrorException();

        Set<Role> roles = new HashSet<>();
        Role ban = banData.get();
        roles.add(ban);
        user.setRoles(roles);

        try {
            userDAO.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FatalErrorException();
        }
        return userUtil.wrapUser(user);
    }

    @Override
    public UserDTO unbanUser(UserWrapper wUser)
            throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException, FatalErrorException {
        String username = stringUtil.checkInputTrim(wUser.getUsername());
        if (username.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("username");

        String authUsername = authorityUtil.getAuthUsername();
        if (username.equals(authUsername)) throw new UserUnauthorizedException();

        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isEmpty()) throw new EntityNotFoundException("user", "username", username);
        User user = userData.get();

        boolean isBanned = authorityUtil.isBanned(user.getRoles());
        if (!isBanned) throw new InvalidInputException("username"); // TODO: This must be more specific.

        Optional<Role> unbanData = roleDAO.findByName(EnumAuthorization.USER.getValue());
        if (unbanData.isEmpty()) throw new FatalErrorException();

        Set<Role> roles = new HashSet<>();
        Role unban = unbanData.get();
        roles.add(unban);
        user.setRoles(roles);

        try {
            userDAO.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FatalErrorException();
        }
        return userUtil.wrapUser(user);
    }
}
