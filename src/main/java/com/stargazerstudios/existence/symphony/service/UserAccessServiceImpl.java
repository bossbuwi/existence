package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.constants.EnumUtilOutput;
import com.stargazerstudios.existence.conductor.erratum.authorization.UserUnauthorizedException;
import com.stargazerstudios.existence.conductor.erratum.database.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.database.EntityDeletionErrorException;
import com.stargazerstudios.existence.conductor.erratum.database.EntitySaveErrorException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityRelationErrorException;
import com.stargazerstudios.existence.conductor.erratum.input.InvalidInputException;
import com.stargazerstudios.existence.conductor.erratum.input.UnexpectedInputException;
import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.utils.AuthorityUtil;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import com.stargazerstudios.existence.sonata.service.EventService;
import com.stargazerstudios.existence.symphony.constants.ConsSymphonyConstraint;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.entity.Role;
import com.stargazerstudios.existence.symphony.entity.User;
import com.stargazerstudios.existence.symphony.repository.RoleDAO;
import com.stargazerstudios.existence.symphony.repository.UserDAO;
import com.stargazerstudios.existence.symphony.utils.UserUtil;
import com.stargazerstudios.existence.symphony.wrapper.AuthWrapper;
import org.hibernate.exception.ConstraintViolationException;
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
    private EventService eventService;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private AuthorityUtil authorityUtil;

    @Autowired
    private StringUtil stringUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Long countUsers() {
        return userDAO.count();
    }

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
    public UserDTO getUser(AuthWrapper wUser) throws EntityErrorException {
        String username = wUser.getUsername();

        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isEmpty()) throw new EntityNotFoundException("user", "username", username);

        User user = userData.get();
        return userUtil.wrapUser(user);
    }

    @Override
    public UserDTO getDetailedUser(long id) throws EntityErrorException {
        Optional<User> userData = userDAO.findById(id);
        if (userData.isEmpty()) throw new EntityNotFoundException("user", "id", Long.toString(id));

        User user = userData.get();
        UserDTO userDTO = userUtil.wrapUser(user);
        userDTO.setEvents_added(eventService.getNumberOfEventsByUser(id));
        userDTO.setLatest_event(eventService.getLastestEventByUser(user.getUsername()));
        return userDTO;
    }

    @Override
    public UserDTO createUser(AuthWrapper wUser)
            throws AuthorizationErrorException,
                EntityErrorException, DatabaseErrorException {
        if (!authorityUtil.checkAuthority(EnumAuthorization.SUPERUSER.getValue())) throw new UserUnauthorizedException();

        String username = wUser.getUsername();
        String password = wUser.getPassword();

        User user = new User();
        user.setUsername(username);
        String hashPassword = passwordEncoder.encode(password);
        user.setPassword(hashPassword);

        Optional<Role> userRole = roleDAO.findByName(EnumAuthorization.USER.getValue());
        if (userRole.isEmpty()) throw new EntityNotFoundException("role", "name", EnumAuthorization.USER.getValue());
        Role role = userRole.get();
        Set<Role> roles = Collections.singleton(role);
        user.setRoles(roles);

        try {
            userDAO.save(user);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            ConstraintViolationException ex = (ConstraintViolationException) e.getCause();
            String constraint = ex.getConstraintName();
            if (constraint.equals(ConsSymphonyConstraint.UNIQUE_USERNAME))
                throw new DuplicateEntityException("user", "username", username);
            throw new EntitySaveErrorException("user");
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("user");
        }

        return userUtil.wrapUser(user);
    }

    @Override
    public UserDTO updateUserPassword(AuthWrapper wUser)
            throws UnknownInputException, AuthorizationErrorException,
                EntityErrorException, DatabaseErrorException {
        String username = wUser.getUsername();
        String oldPassword = wUser.getOld_password();
        String newPassword = wUser.getNew_password();
        String confirmPassword = wUser.getConfirm_password();

        if (!newPassword.equals(confirmPassword))
            throw new UnexpectedInputException("Password confirmation does not match the new password.");

        String authUsername = authorityUtil.getAuthUsername();
        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.SUPERUSER.getValue());

        if (!authUsername.equals(username) && !isAuthorized) throw new UserUnauthorizedException();

        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isEmpty()) throw new EntityNotFoundException("user", "username", username);
        User user = userData.get();
        if (!passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new UnexpectedInputException("Password provided does not match the current password.");
        if (oldPassword.equals(newPassword))
            throw new UnexpectedInputException("New password is the same as the old password.");
        String hashPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashPassword);

        try {
            userDAO.save(user);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            ConstraintViolationException ex = (ConstraintViolationException) e.getCause();
            String constraint = ex.getConstraintName();
            if (constraint.equals(ConsSymphonyConstraint.UNIQUE_USERNAME))
                throw new DuplicateEntityException("user", "username", username);
            throw new EntitySaveErrorException("user");
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("user");
        }

        return userUtil.wrapUser(user);
    }

    @Override
    public UserDTO deleteUser(AuthWrapper wUser)
            throws UnknownInputException, AuthorizationErrorException,
                EntityErrorException, DatabaseErrorException{
        boolean isSuperUserOrHigher = authorityUtil.checkAuthority(EnumAuthorization.SUPERUSER.getValue());
        if (!isSuperUserOrHigher) throw new UserUnauthorizedException();

        String username = wUser.getUsername();

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
            throw new EntityDeletionErrorException("user");
        }
        return userUtil.wrapUser(user);
    }

    @Override
    public UserDTO addRoles(AuthWrapper wUser)
            throws AuthorizationErrorException, UnknownInputException,
                EntityErrorException, DatabaseErrorException {
        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.SUPERUSER.getValue());
        if (!isAuthorized) throw new UserUnauthorizedException();

        String username = wUser.getUsername();

        String authUsername = authorityUtil.getAuthUsername();
        if (username.equals(authUsername)) throw new UserUnauthorizedException();

        String[] rolesIn = wUser.getRoles();
        Set<String> rolesQuery = new HashSet<>();
        for (String role: rolesIn) {
            if (!stringUtil.checkInputTrim(role).equals(EnumUtilOutput.EMPTY.getValue())) {
                rolesQuery.add(role);
            } else {
                throw new InvalidInputException("roles");
            }
        }

        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isEmpty()) throw new EntityNotFoundException("user", "username", username);
        User user = userData.get();

        if (authorityUtil.isBanned(user.getRoles())) throw new InvalidInputException("username");

        long userRank = authorityUtil.getHighestRank(user);
        long authRank = authorityUtil.getHighestRank();
        if (authorityUtil.hasHigherRank(userRank, authRank)) throw new UserUnauthorizedException();

        List<Role> rolesDb = roleDAO.findRolesBySet(rolesQuery);
        if (rolesDb.size() != rolesQuery.size()) throw new InvalidInputException("roles");

        long inputRank = authorityUtil.getHighestRank(rolesDb);
        if (authorityUtil.hasHigherRank(inputRank, authRank)) throw new UserUnauthorizedException();

        Set<Role> userRoles = user.getRoles();
        Set<Role> addRoles = new HashSet<>(rolesDb);
        userRoles.addAll(addRoles);

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
    public UserDTO removeRoles(AuthWrapper wUser)
            throws AuthorizationErrorException, UnknownInputException,
                EntityErrorException, DatabaseErrorException {
        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.SUPERUSER.getValue());
        if (!isAuthorized) throw new UserUnauthorizedException();

        String username = wUser.getUsername();

        String authUsername = authorityUtil.getAuthUsername();
        if (username.equals(authUsername)) throw new UserUnauthorizedException();

        String[] rolesIn = wUser.getRoles();
        Set<String> rolesQuery = new HashSet<>();
        for (String role: rolesIn) {
            if (!stringUtil.checkInputTrim(role).equals(EnumUtilOutput.EMPTY.getValue())) {
                rolesQuery.add(role);
            } else {
                throw new InvalidInputException("roles");
            }
        }

        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isEmpty()) throw new EntityNotFoundException("user", "username", username);
        User user = userData.get();
        if (authorityUtil.isBanned(user.getRoles())) throw new InvalidInputException("username");

        long userRank = authorityUtil.getHighestRank(user);
        long authRank = authorityUtil.getHighestRank();
        if (authorityUtil.hasHigherRank(userRank, authRank)) throw new UserUnauthorizedException();

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
                throw new EntityRelationErrorException("User does not have a role with: " + roleName);
            }
        }

        userRoles.removeAll(removeRoles);
        if (userRoles.size() == 1) throw new EntityRelationErrorException("User should have at least one role.");

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
    public UserDTO banUser(AuthWrapper wUser)
            throws UnknownInputException, AuthorizationErrorException,
                EntityErrorException, DatabaseErrorException {
        String username = wUser.getUsername();

        boolean isAdminOrHigher = authorityUtil.checkAuthority(EnumAuthorization.ADMIN.getValue());
        if (!isAdminOrHigher) throw new UserUnauthorizedException();

        String authUsername = authorityUtil.getAuthUsername();
        if (username.equals(authUsername)) throw new UserUnauthorizedException();

        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isEmpty()) throw new EntityNotFoundException("user", "username", username);
        User user = userData.get();

        boolean isBanned = authorityUtil.isBanned(user.getRoles());
        if (isBanned) throw new UnexpectedInputException("User with username: " + user.getUsername() + " is already banned.");

        Optional<Role> banData = roleDAO.findByName(EnumAuthorization.BANNED.getValue());
        if (banData.isEmpty()) throw new EntityNotFoundException("role", "name", EnumAuthorization.BANNED.getValue());

        Set<Role> roles = new HashSet<>();
        Role ban = banData.get();
        roles.add(ban);
        user.setRoles(roles);

        try {
            userDAO.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("user");
        }
        return userUtil.wrapUser(user);
    }

    @Override
    public UserDTO unbanUser(AuthWrapper wUser)
            throws UnknownInputException, AuthorizationErrorException,
                EntityErrorException, DatabaseErrorException {
        String username = wUser.getUsername();

        boolean isAdminOrHigher = authorityUtil.checkAuthority(EnumAuthorization.ADMIN.getValue());
        if (!isAdminOrHigher) throw new UserUnauthorizedException();

        String authUsername = authorityUtil.getAuthUsername();
        if (username.equals(authUsername)) throw new UserUnauthorizedException();

        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isEmpty()) throw new EntityNotFoundException("user", "username", username);
        User user = userData.get();

        boolean isBanned = authorityUtil.isBanned(user.getRoles());
        if (!isBanned) throw new UnexpectedInputException("User with username: " + user.getUsername() + " is not banned.");

        Optional<Role> unbanData = roleDAO.findByName(EnumAuthorization.USER.getValue());
        if (unbanData.isEmpty()) throw new EntityNotFoundException("role", "name", EnumAuthorization.BANNED.getValue());

        Set<Role> roles = new HashSet<>();
        Role unban = unbanData.get();
        roles.add(unban);
        user.setRoles(roles);

        try {
            userDAO.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("user");
        }
        return userUtil.wrapUser(user);
    }
}
