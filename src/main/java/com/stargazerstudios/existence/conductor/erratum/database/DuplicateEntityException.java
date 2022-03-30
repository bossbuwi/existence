package com.stargazerstudios.existence.conductor.erratum.database;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;

public class DuplicateEntityException extends DatabaseErrorException {

    public DuplicateEntityException(String entityName, String key, String value) {
        super(entityName + " with " + key + ": " + value + " already exists.");
    }
}
