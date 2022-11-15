package com.stargazerstudios.existence.conductor.erratum.thirdparty;

import com.stargazerstudios.existence.conductor.erratum.root.ThirdPartyException;

public class GatewayException extends ThirdPartyException {

    public GatewayException() {
        super("Third party server returned an unknown error.");
    }
}
