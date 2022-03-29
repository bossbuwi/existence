package com.stargazerstudios.existence.conductor.erratum.input;

import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;

public class EmptyInputException extends UnknownInputException {

    public EmptyInputException(String collection) {
        super("A key on collection: " + collection + " contains blank values. " +
                "Please restructure the request and try again.");
    }
}
