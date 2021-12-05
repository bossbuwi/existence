package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.universal.BadGatewayException;
import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.universal.GatewayTimeoutException;
import com.stargazerstudios.existence.conductor.erratum.universal.UserNotFoundException;
import com.stargazerstudios.existence.symphony.dto.UserDTO;

public interface AuthenticationService {
    UserDTO login(String username, String password)
            throws UserNotFoundException, BadGatewayException, GatewayTimeoutException, EntityNotFoundException;
}
