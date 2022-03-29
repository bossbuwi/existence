package com.stargazerstudios.existence.conductor.erratum.entity;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;

public class DuplicateEntityException extends DatabaseErrorException {

    public DuplicateEntityException(String entityName, String key, String value) {
        super(entityName + " with " + key + ": " + value + " already exists.");
    }
}
