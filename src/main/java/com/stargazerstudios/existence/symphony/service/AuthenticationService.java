package com.stargazerstudios.existence.symphony.service;


import com.stargazerstudios.existence.conductor.erratum.root.*;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.wrapper.UserWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    UserDTO login(UserWrapper user)
            throws UnknownInputException, AuthorizationErrorException,
                SystemErrorException, ThirdPartyErrorException,
                DatabaseErrorException, EntityErrorException;
    UserDTO autologin(String token) throws AuthorizationErrorException;
    boolean logout(HttpServletRequest request, HttpServletResponse response);
}
