package com.stargazerstudios.existence.conductor.erratum.system;

import com.stargazerstudios.existence.conductor.erratum.root.SystemErrorException;

public class InactiveSwitchableFeatureException extends SystemErrorException {

    public InactiveSwitchableFeatureException(String switchableFeature) {
        super("This service needs feature: " + switchableFeature + " to be active." +
                " Please contact an admin.");
    }
}
