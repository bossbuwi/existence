package com.stargazerstudios.existence.conductor.erratum.universal;

public class EntitySaveErrorException extends DatabaseErrorException {

    public EntitySaveErrorException(String entityName) {
        super("There is an error while saving the " + entityName + ".");
    }
}
