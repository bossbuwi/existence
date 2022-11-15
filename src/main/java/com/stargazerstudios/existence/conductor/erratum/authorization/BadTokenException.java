package com.stargazerstudios.existence.conductor.erratum.authorization;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;

public class BadTokenException extends AuthorizationException {

    public BadTokenException() {
        super("Token is either expired or invalid. Please go through user authentication again.");
    }
}
