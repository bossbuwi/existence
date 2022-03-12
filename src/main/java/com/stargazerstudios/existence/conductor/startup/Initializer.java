package com.stargazerstudios.existence.conductor.startup;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.constants.EnumUtilOutput;
import com.stargazerstudios.existence.conductor.erratum.universal.FatalErrorException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidPropertyErrorException;
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

    @EventListener(ApplicationReadyEvent.class)
    public void postStartUp() throws FatalErrorException, InvalidPropertyErrorException {
        createDefaultAdmin();
        checkJwtKey();
    }

    private void createDefaultAdmin() throws FatalErrorException {
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

    private void checkJwtKey() throws InvalidPropertyErrorException {
        try {
            if (stringUtil.checkInput(jwtKey).equals(EnumUtilOutput.EMPTY.getValue()))
                throw new InvalidPropertyErrorException("jwt.secret");
        } catch (InvalidPropertyErrorException e) {
            e.printStackTrace();
        }
    }
}
