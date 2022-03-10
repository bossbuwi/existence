package com.stargazerstudios.existence.conductor.erratum.universal;

import org.springframework.http.HttpStatus;

public class AppUninitializedErrorException extends Exception{

    @Override
    public String getMessage() {
        return "The application has not been initialized. Please contact the admin.";
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
