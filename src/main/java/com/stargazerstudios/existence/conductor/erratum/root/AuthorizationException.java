package com.stargazerstudios.existence.conductor.erratum.root;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends Exception {

    public AuthorizationException(String message) {
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
