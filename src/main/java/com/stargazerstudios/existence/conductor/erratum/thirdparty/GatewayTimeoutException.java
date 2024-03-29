package com.stargazerstudios.existence.conductor.erratum.thirdparty;

import com.stargazerstudios.existence.conductor.erratum.root.ThirdPartyException;
import org.springframework.http.HttpStatus;

public class GatewayTimeoutException extends ThirdPartyException {

    public GatewayTimeoutException() {
        super("Third party server returned a time out error. Please try again.");
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.GATEWAY_TIMEOUT;
    }
}
