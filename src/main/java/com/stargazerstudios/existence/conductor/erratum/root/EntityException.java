package com.stargazerstudios.existence.conductor.erratum.root;

import org.springframework.http.HttpStatus;

public class EntityException extends Exception {

    public EntityException(String message) {
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
