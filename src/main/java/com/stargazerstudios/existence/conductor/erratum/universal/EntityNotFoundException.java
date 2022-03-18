package com.stargazerstudios.existence.conductor.erratum.universal;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends Exception{

    public EntityNotFoundException(String entity, String key, String value) {
        super(entity + " with " + key + ": " + value + " not found.");
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
