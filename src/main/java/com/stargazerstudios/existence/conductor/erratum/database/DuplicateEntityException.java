package com.stargazerstudios.existence.conductor.erratum.database;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;

public class DuplicateEntityException extends DatabaseException {

    public DuplicateEntityException(String entityName, String key, String value) {
        super(entityName + " with " + key + ": " + value + " already exists.");
    }
}
