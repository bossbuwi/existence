package com.stargazerstudios.existence.conductor.erratum.system;

import com.stargazerstudios.existence.conductor.erratum.root.SystemException;

public class FeatureNotImplementedException extends SystemException {

    public FeatureNotImplementedException(String message) {
        super(message);
    }
}
