package com.stargazerstudios.existence.conductor.erratum.authorization;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import org.springframework.http.HttpStatus;

public class UserUnauthorizedException extends AuthorizationException {

    public UserUnauthorizedException() {
        super("User with the provided credentials not authorized for this action.");
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
