package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.universal.*;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.wrapper.UserWrapper;

public interface AuthenticationService {
    UserDTO login(UserWrapper user)
            throws UserNotFoundException, BadGatewayException, EntityNotFoundException, InvalidInputException, FatalErrorException, InvalidPropertyErrorException;
    UserDTO autologin(String token) throws UserNotFoundException;
}
