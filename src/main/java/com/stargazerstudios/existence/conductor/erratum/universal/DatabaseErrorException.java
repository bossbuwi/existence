package com.stargazerstudios.existence.conductor.erratum.universal;

import org.springframework.http.HttpStatus;

public class DatabaseErrorException extends Exception{
    // TODO: This needs to be reworked to inform the user of the specific error.
    public DatabaseErrorException(String message) {
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
