package com.stargazerstudios.existence.conductor.erratum.thirdparty;

import com.stargazerstudios.existence.conductor.erratum.root.ThirdPartyErrorException;

public class GatewayErrorException extends ThirdPartyErrorException {

    public GatewayErrorException() {
        super("Third party server returned an unknown error.");
    }
}
