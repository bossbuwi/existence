package com.stargazerstudios.existence.conductor.erratum.input;

import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;

public class UnexpectedInputException extends UnknownInputException {

    public UnexpectedInputException(String message) {
        super(message);
    }
}
