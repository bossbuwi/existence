package com.stargazerstudios.existence.conductor.erratum.database;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;

public class DependentEntityException extends DatabaseErrorException {

    public DependentEntityException (String entity, String identifier) {
        super(entity + ": " + identifier + " has active dependencies. " +
                "It may not be deleted until all of the dependencies are deleted first.");
    }
}
