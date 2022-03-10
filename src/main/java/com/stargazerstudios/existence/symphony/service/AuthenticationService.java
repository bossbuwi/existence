package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.universal.*;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.wrapper.UserWrapper;

public interface AuthenticationService {
    UserDTO login(UserWrapper user)
            throws UserNotFoundException, BadGatewayException, GatewayTimeoutException, EntityNotFoundException, InvalidInputException, AppUninitializedErrorException;
    UserDTO autologin(String token) throws UserNotFoundException;
}
