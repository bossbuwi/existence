package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.universal.*;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.wrapper.UserWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    UserDTO login(UserWrapper user)
            throws UserNotFoundException, BadGatewayException, EntityNotFoundException, InvalidInputException, FatalErrorException, InvalidPropertyErrorException, UserUnauthorizedException, EntitySaveErrorException;
    UserDTO autologin(String token) throws UserNotFoundException;
    boolean logout(HttpServletRequest request, HttpServletResponse response);
}
