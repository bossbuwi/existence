package com.stargazerstudios.existence.conductor.erratum.authorization;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;

public class BadTokenException extends AuthorizationErrorException {

    public BadTokenException() {
        super("Token is either expired or invalid. Please go through user authentication again.");
    }
}
