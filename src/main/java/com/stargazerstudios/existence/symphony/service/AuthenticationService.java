package com.stargazerstudios.existence.symphony.service;


import com.stargazerstudios.existence.conductor.erratum.root.*;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.wrapper.AuthWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    UserDTO login(AuthWrapper user)
            throws UnknownInputException, AuthorizationException,
            SystemException, ThirdPartyException,
            DatabaseException, EntityException;
    UserDTO autologin(String token) throws AuthorizationException;
    boolean logout(HttpServletRequest request, HttpServletResponse response);
}
