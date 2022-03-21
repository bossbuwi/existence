package com.stargazerstudios.existence.conductor.erratum.entity;

import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends EntityErrorException {

    public EntityNotFoundException(String entityName, String key, String value) {
        super(entityName + " with " + key + ": " + value + " not found.");
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
