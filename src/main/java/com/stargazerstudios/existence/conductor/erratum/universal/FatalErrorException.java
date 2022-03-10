package com.stargazerstudios.existence.conductor.erratum.universal;

import org.springframework.http.HttpStatus;

public class FatalErrorException extends Exception{

    @Override
    public String getMessage() {
        return "The application has encountered a fatal error. Please contact the admin.";
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
