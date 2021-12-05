package com.stargazerstudios.existence.conductor.erratum.universal;

import org.springframework.http.HttpStatus;

public class UserUnauthorizedException extends Exception{

    @Override
    public String getMessage() {
        return "User with the provided credentials not authorized for this action.";
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
