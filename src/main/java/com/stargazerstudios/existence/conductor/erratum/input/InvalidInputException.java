package com.stargazerstudios.existence.conductor.erratum.input;

import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;

public class InvalidInputException extends UnknownInputException {

    public InvalidInputException(String key) {
        super("Key: " + key + " - not found on the input or contains invalid values. " +
                "Please restructure the request and try again.");
    }

    public InvalidInputException(String key, String collection) {
        super("Key: " + key + " on collection: " + collection + " - not found on the input or contains invalid values. " +
                "Please restructure the request and try again.");
    }
}
