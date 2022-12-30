package com.stargazerstudios.existence.conductor.erratum.system;

import com.stargazerstudios.existence.conductor.erratum.root.SystemException;

public class InvalidPropertyException extends SystemException {

    public InvalidPropertyException(String key) {
        super("Invalid application property key: " + key + ". Please contact admin.");
    }
}
