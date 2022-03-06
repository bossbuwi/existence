package com.stargazerstudios.existence.conductor.erratum.universal;

public class InvalidCollectionException extends InvalidInputException {

    public InvalidCollectionException(String collection) {
        super("Collection: " + collection + " - not found on the input or contains invalid values. " +
                "Please restructure the request and try again.");
    }
}
