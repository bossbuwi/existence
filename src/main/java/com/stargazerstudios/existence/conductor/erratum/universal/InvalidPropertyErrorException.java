package com.stargazerstudios.existence.conductor.erratum.universal;

import org.springframework.http.HttpStatus;

public class InvalidPropertyErrorException extends Exception {

    public InvalidPropertyErrorException(String key) {
        super("Invalid application property key: " + key + ". Please contact admin.");
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
