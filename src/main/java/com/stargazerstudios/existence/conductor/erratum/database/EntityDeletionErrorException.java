package com.stargazerstudios.existence.conductor.erratum.database;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;

public class EntityDeletionErrorException extends DatabaseErrorException {

    public EntityDeletionErrorException(String entityName) {
        super("There is an error while deleting the " + entityName + " entity.");
    }
}
