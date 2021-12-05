package com.stargazerstudios.existence.conductor.erratum.universal;

import org.springframework.http.HttpStatus;

public class UserNotLoggedInException extends Exception{

    @Override
    public String getMessage() {
        return "User needs to login to access the information. Please log in first.";
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
