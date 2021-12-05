package com.stargazerstudios.existence.conductor.erratum.universal;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends Exception{

    @Override
    public String getMessage() {
        return "User with the provided credentials not found.";
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
