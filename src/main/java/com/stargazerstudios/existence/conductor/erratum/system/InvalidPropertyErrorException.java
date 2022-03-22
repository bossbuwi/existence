package com.stargazerstudios.existence.conductor.erratum.system;

import com.stargazerstudios.existence.conductor.erratum.root.SystemErrorException;

public class InvalidPropertyErrorException extends SystemErrorException {

    public InvalidPropertyErrorException(String key) {
        super("Invalid application property key: " + key + ". Please contact admin.");
    }
}
