package com.stargazerstudios.existence.conductor.erratum.database;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;

public class EntitySaveErrorException extends DatabaseErrorException {

    public EntitySaveErrorException(String entityName) {
        super("There is an error while saving the " + entityName + ".");
    }
}
