package com.stargazerstudios.existence.conductor.erratum.database;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;

public class EntitySaveException extends DatabaseException {

    public EntitySaveException(String entityName) {
        super("There is an error while saving the " + entityName + ".");
    }
}
