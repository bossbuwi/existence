package com.stargazerstudios.existence.conductor.startup;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.constants.EnumRank;
import com.stargazerstudios.existence.conductor.constants.EnumUtilOutput;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.system.FatalErrorException;
import com.stargazerstudios.existence.conductor.erratum.system.InvalidPropertyErrorException;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
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

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private StringUtil stringUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtKey;

    /**
     * Automatically executed upon application startup.
     * It calls methods that validate integral parts of the app.
     */
    @EventListener(ApplicationReadyEvent.class)
    private void postStartUp() {
        checkDefaultAdmin();
        checkJwtKey();
        checkRoles();
    }

    /**
     * Checks if the default admin account has been set up. If no admin account is found, it automatically creates
     * one and grants it maximum authority.
     */
    private void checkDefaultAdmin() {
        Optional<User> adminData = userDAO.findByUsername(EnumAuthorization.DEFAULT_USER.getValue());
        if (adminData.isEmpty()) {
            User admin = new User();
            admin.setUsername(EnumAuthorization.DEFAULT_USER.getValue());
            String hashPword = passwordEncoder.encode(EnumAuthorization.DEFAULT_USER.getValue());
            admin.setPassword(hashPword);

            Set<String> roleNames = new HashSet<>(Arrays.asList(
                    EnumAuthorization.OWNER.getValue(),
                    EnumAuthorization.SUPERUSER.getValue(),
                    EnumAuthorization.ADMIN.getValue(),
                    EnumAuthorization.USER.getValue()
            ));

            List<Role> dbRoles = roleDAO.findRolesBySet(roleNames);
            try {
                if (dbRoles.size() != roleNames.size()) throw new FatalErrorException();
            } catch (FatalErrorException e) {
                e.printStackTrace();
            }

            Set<Role> roles = new HashSet<>(dbRoles);
            admin.setRoles(roles);

            try {
                userDAO.save(admin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if the property "jwt.secret" has been defined on the application.properties file.
     * If not, this throws an error that is readable on the system logs.
     */
    private void checkJwtKey() {
        try {
            if (stringUtil.checkInput(jwtKey).equals(EnumUtilOutput.EMPTY.getValue()))
                throw new InvalidPropertyErrorException("jwt.secret");
        } catch (InvalidPropertyErrorException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks the integrity of the roles saved in the database. If it finds a discrepancy, it throws an error
     * that is readable on the system logs.
     */
    private void checkRoles() {
        Set<String> roleNames = new HashSet<>();
        for (EnumAuthorization auth: EnumAuthorization.values()) {
            if (!auth.equals(EnumAuthorization.DEFAULT_USER)) roleNames.add(auth.getValue());
        }

        final List<Role> roles = roleDAO.findRolesBySet(roleNames);

        try {
            if (roles.size() != roleNames.size()) throw new DatabaseErrorException("The roles database has been tampered with." +
                    " Please contact an admin.");
        } catch (DatabaseErrorException e) {
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
                if (role == null) throw new DatabaseErrorException("The roles database has been tampered with." +
                            " Please contact an admin.");
                if (!role.getName().equals(rank.toString())) throw new DatabaseErrorException("The roles database has been tampered with." +
                        " Please contact an admin.");
            }
        } catch (DatabaseErrorException e) {
            e.printStackTrace();
        }
    }
}
