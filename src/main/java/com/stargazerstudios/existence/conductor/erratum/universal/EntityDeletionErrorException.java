package com.stargazerstudios.existence.conductor.erratum.universal;

public class EntityDeletionErrorException extends DatabaseErrorException{

    public EntityDeletionErrorException(String entityName) {
        super("There is an error while deleting the " + entityName + " entity.");
    }
}
