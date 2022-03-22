package com.stargazerstudios.existence.conductor.erratum.input;

import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;

public class InvalidCollectionException extends UnknownInputException {
    // TODO: This must be reworked. It is uninformative.
    public InvalidCollectionException(String collection) {
        super("Collection: " + collection + " - not found on the input or contains invalid values. " +
                "Please restructure the request and try again.");
    }
}
