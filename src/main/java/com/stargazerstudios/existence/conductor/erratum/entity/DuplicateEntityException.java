package com.stargazerstudios.existence.conductor.erratum.entity;

import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;

public class DuplicateEntityException extends EntityErrorException {

    public DuplicateEntityException(String entityName, String key, String value) {
        super(entityName + " with " + key + ": " + value + " already exists.");
    }
}
