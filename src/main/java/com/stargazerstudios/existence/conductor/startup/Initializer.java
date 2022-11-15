package com.stargazerstudios.existence.conductor.startup;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.constants.EnumRank;
import com.stargazerstudios.existence.conductor.constants.EnumUtilOutput;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.system.FatalException;
import com.stargazerstudios.existence.conductor.erratum.system.InvalidPropertyException;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import com.stargazerstudios.existence.eligius.service.FileProcessorServiceImpl;
import com.stargazerstudios.existence.symphony.entity.Role;
import com.stargazerstudios.existence.symphony.entity.User;
import com.stargazerstudios.existence.symphony.repository.RoleDAO;
import com.stargazerstudios.existence.symphony.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Initializer {

    public static boolean HAS_ERRORS = false;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private StringUtil stringUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileProcessorServiceImpl fileProcessorService;

    @Value("${jwt.secret}")
    private String jwtKey;

    /**
     * Automatically executed upon application startup.
     * It calls methods that validate integral parts of the app.
     */
    @EventListener(ApplicationReadyEvent.class)
    private void postStartUp() {
        HAS_ERRORS = checkRoles() || checkJwtKey() || checkDefaultOwner()
                || checkDefaultUser() || validateFileImportDirectory();
    }

    /**
     * Checks if the default owner account has been set up.
     * If no owner account is found, it automatically creates
     * one and grants it maximum authority.
     * @return boolean true if the method encountered an error.
     */
    private boolean checkDefaultOwner() {
        Optional<User> ownerData = userDAO.findByUsername(EnumAuthorization.DEFAULT_OWNER.getValue());
        if (ownerData.isEmpty()) {
            User owner = new User();
            owner.setUsername(EnumAuthorization.DEFAULT_OWNER.getValue());
            String hashPword = passwordEncoder.encode(EnumAuthorization.DEFAULT_OWNER.getValue());
            owner.setPassword(hashPword);

            Set<String> roleNames = new HashSet<>(Arrays.asList(
                    EnumAuthorization.OWNER.getValue(),
                    EnumAuthorization.SUPERUSER.getValue(),
                    EnumAuthorization.ADMIN.getValue(),
                    EnumAuthorization.USER.getValue()
            ));

            List<Role> dbRoles = roleDAO.findRolesBySet(roleNames);
            try {
                if (dbRoles.size() != roleNames.size()) throw new FatalException();
            } catch (FatalException e) {
                e.printStackTrace();
            }

            Set<Role> roles = new HashSet<>(dbRoles);
            owner.setRoles(roles);

            try {
                userDAO.save(owner);
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the default user account has been set up.
     * If no user account is found, it automatically creates
     * one and grants it standard authority.
     * @return boolean true if the method encountered an error.
     */
    private boolean checkDefaultUser() {
        Optional<User> userData = userDAO.findByUsername(EnumAuthorization.DEFAULT_USER.getValue());
        if (userData.isEmpty()) {
            User user = new User();
            user.setUsername(EnumAuthorization.DEFAULT_USER.getValue());
            String hashPword = passwordEncoder.encode(EnumAuthorization.DEFAULT_USER.getValue());
            user.setPassword(hashPword);

            Optional<Role> userRole = roleDAO.findByName(EnumAuthorization.USER.getValue());
            Role role = userRole.get();

            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);

            try {
                userDAO.save(user);
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the property "jwt.secret" has been defined on the application.properties file.
     * If not, this throws an error that is readable on the system logs.
     * @return boolean true if the method encountered an error.
     */
    private boolean checkJwtKey() {
        try {
            if (stringUtil.checkInput(jwtKey).equals(EnumUtilOutput.EMPTY.getValue()))
                throw new InvalidPropertyException("jwt.secret");
        } catch (InvalidPropertyException e) {
            e.printStackTrace();
            return true;
        }

        return false;
    }

    /**
     * Checks the integrity of the roles saved in the database. If it finds a discrepancy, it throws an error
     * that is readable on the system logs.
     * @return boolean true if the method encountered an error.
     */
    private boolean checkRoles() {
        Set<String> roleNames = new HashSet<>();
        for (EnumAuthorization auth: EnumAuthorization.values()) {
            if (!auth.equals(EnumAuthorization.DEFAULT_OWNER) && !auth.equals(EnumAuthorization.DEFAULT_USER)) {
                roleNames.add(auth.getValue());
            }
        }

        final List<Role> roles = roleDAO.findRolesBySet(roleNames);

        try {
            if (roles.size() != roleNames.size()) throw new DatabaseException("The roles database has been tampered with." +
                    " Please contact an admin.");
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        try {
            for (EnumRank rank: EnumRank.values()) {
                Role role = roles.stream()
                        .filter(
                                x -> x.getRank() == rank.getValue()
                        )
                        .findAny()
                        .orElse(null);
                if (role == null) throw new DatabaseException("The roles database has been tampered with." +
                            " Please contact an admin.");
                if (!role.getName().equals(rank.toString())) throw new DatabaseException("The roles database has been tampered with." +
                        " Please contact an admin.");
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
            return true;
        }

        return false;
    }

    private boolean validateFileImportDirectory() {
        try {
            fileProcessorService.deleteAll();
            fileProcessorService.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
