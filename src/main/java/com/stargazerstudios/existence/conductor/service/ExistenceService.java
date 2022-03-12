package com.stargazerstudios.existence.conductor.service;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.constants.EnumUtilOutput;
import com.stargazerstudios.existence.conductor.erratum.universal.FatalErrorException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidPropertyErrorException;
import com.stargazerstudios.existence.conductor.erratum.universal.UserUnauthorizedException;
import com.stargazerstudios.existence.conductor.model.ExistenceIdentity;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.entity.User;
import com.stargazerstudios.existence.symphony.repository.UserDAO;
import com.stargazerstudios.existence.symphony.utils.UserUtil;
import com.stargazerstudios.existence.symphony.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ExistenceService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private StringUtil stringUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${master.password}")
    private String masterPassword;

    public ExistenceIdentity realizeDreams() {
        return new ExistenceIdentity();
    }

    public UserDTO startOver(UserWrapper wUser)
            throws UserUnauthorizedException, FatalErrorException, InvalidPropertyErrorException {
        String username = stringUtil.checkInput(wUser.getUsername());
        String password = stringUtil.checkInput(wUser.getPassword());

        if (!username.equals(EnumAuthorization.DEFAULT_USER.getValue())) throw new UserUnauthorizedException();
        if (username.equals(EnumUtilOutput.EMPTY.getValue())) throw new UserUnauthorizedException();
        if (password.equals(EnumUtilOutput.EMPTY.getValue())) throw new UserUnauthorizedException();

        if (stringUtil.checkInput(masterPassword).equals(EnumUtilOutput.EMPTY.getValue()))
            throw new InvalidPropertyErrorException("master.password");

        if (!password.equals(masterPassword)) throw new UserUnauthorizedException();

        Optional<User> adminData = userDAO.findByUsername(username);
        if (adminData.isEmpty()) throw new FatalErrorException();

        User admin = adminData.get();
        String hashPassword = passwordEncoder.encode(EnumAuthorization.DEFAULT_USER.getValue());
        admin.setPassword(hashPassword);

        try {
            userDAO.save(admin);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FatalErrorException();
        }

        return userUtil.wrapUser(admin);
    }
}
