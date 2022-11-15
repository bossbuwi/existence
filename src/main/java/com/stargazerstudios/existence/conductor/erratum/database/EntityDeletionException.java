package com.stargazerstudios.existence.conductor.erratum.database;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;

public class EntityDeletionException extends DatabaseException {

    public EntityDeletionException(String entityName) {
        super("There is an error while deleting the " + entityName + " entity.");
    }
}
