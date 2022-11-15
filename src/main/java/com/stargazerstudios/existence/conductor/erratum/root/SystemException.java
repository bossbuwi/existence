package com.stargazerstudios.existence.conductor.erratum.root;

import org.springframework.http.HttpStatus;

public class SystemException extends Exception {

    public SystemException(String message) {
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.SERVICE_UNAVAILABLE;
    }
}
