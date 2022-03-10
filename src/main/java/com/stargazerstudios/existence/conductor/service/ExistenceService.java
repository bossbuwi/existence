package com.stargazerstudios.existence.conductor.service;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.erratum.universal.FatalErrorException;
import com.stargazerstudios.existence.conductor.model.ExistenceIdentity;
import com.stargazerstudios.existence.symphony.entity.Role;
import com.stargazerstudios.existence.symphony.entity.User;
import com.stargazerstudios.existence.symphony.repository.RoleDAO;
import com.stargazerstudios.existence.symphony.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ExistenceService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ExistenceIdentity realizeDreams() throws FatalErrorException {
        Optional<User> userData = userDAO.findByUsername(EnumAuthorization.DEFAULT_USER.getValue());
        if (userData.isEmpty()) {
            boolean isDone = hasAdmin();
            if (isDone) return new ExistenceIdentity();
            else throw new FatalErrorException();
        } else {
            return new ExistenceIdentity();
        }
    }

    private boolean hasAdmin() throws FatalErrorException {
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
        if (dbRoles.size() != roleNames.size()) throw new FatalErrorException();

        Set<Role> roles = new HashSet<>(dbRoles);
        admin.setRoles(roles);
        userDAO.save(admin);
        return true;
    }
}
