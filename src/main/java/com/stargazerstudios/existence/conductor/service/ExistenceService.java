package com.stargazerstudios.existence.conductor.service;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.constants.EnumUtilOutput;
import com.stargazerstudios.existence.conductor.erratum.authorization.UserUnauthorizedException;
import com.stargazerstudios.existence.conductor.erratum.root.SystemErrorException;
import com.stargazerstudios.existence.conductor.erratum.system.FatalErrorException;
import com.stargazerstudios.existence.conductor.erratum.system.InvalidPropertyErrorException;
import com.stargazerstudios.existence.conductor.model.ExistenceIdentity;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.entity.Role;
import com.stargazerstudios.existence.symphony.entity.User;
import com.stargazerstudios.existence.symphony.repository.RoleDAO;
import com.stargazerstudios.existence.symphony.repository.UserDAO;
import com.stargazerstudios.existence.symphony.utils.UserUtil;
import com.stargazerstudios.existence.symphony.wrapper.AuthWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private UserUtil userUtil;

    @Autowired
    private StringUtil stringUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${master.password}")
    private String masterPassword;

    public ExistenceIdentity instigate() {
        return new ExistenceIdentity();
    }

    public UserDTO resetAdminPassword(AuthWrapper wUser)
            throws UserUnauthorizedException, SystemErrorException {
        String username = stringUtil.checkInput(wUser.getUsername());
        String password = stringUtil.checkInput(wUser.getPassword());

        if (!username.equals(EnumAuthorization.DEFAULT_OWNER.getValue())) throw new UserUnauthorizedException();
        if (username.equals(EnumUtilOutput.EMPTY.getValue())) throw new UserUnauthorizedException();
        if (password.equals(EnumUtilOutput.EMPTY.getValue())) throw new UserUnauthorizedException();

        if (stringUtil.checkInput(masterPassword).equals(EnumUtilOutput.EMPTY.getValue()))
            throw new InvalidPropertyErrorException("master.password");

        if (!password.equals(masterPassword)) throw new UserUnauthorizedException();

        Optional<User> adminData = userDAO.findByUsername(username);
        if (adminData.isEmpty()) throw new FatalErrorException();

        User admin = adminData.get();
        String hashPassword = passwordEncoder.encode(EnumAuthorization.DEFAULT_OWNER.getValue());
        admin.setPassword(hashPassword);

        try {
            userDAO.save(admin);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FatalErrorException();
        }

        return userUtil.wrapUser(admin);
    }

    public UserDTO resetAdminRoles(AuthWrapper wUser)
            throws UserUnauthorizedException, SystemErrorException {
        String username = stringUtil.checkInput(wUser.getUsername());
        String password = stringUtil.checkInput(wUser.getPassword());

        if (!username.equals(EnumAuthorization.DEFAULT_OWNER.getValue())) throw new UserUnauthorizedException();
        if (username.equals(EnumUtilOutput.EMPTY.getValue())) throw new UserUnauthorizedException();
        if (password.equals(EnumUtilOutput.EMPTY.getValue())) throw new UserUnauthorizedException();

        if (stringUtil.checkInput(masterPassword).equals(EnumUtilOutput.EMPTY.getValue()))
            throw new InvalidPropertyErrorException("master.password");

        if (!password.equals(masterPassword)) throw new UserUnauthorizedException();

        Optional<User> adminData = userDAO.findByUsername(username);
        if (adminData.isEmpty()) throw new FatalErrorException();

        User admin = adminData.get();
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
            throw new FatalErrorException();
        }

        return userUtil.wrapUser(admin);
    }
}
