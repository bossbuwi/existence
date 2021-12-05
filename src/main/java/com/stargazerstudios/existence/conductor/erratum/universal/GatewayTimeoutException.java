package com.stargazerstudios.existence.conductor.erratum.universal;

import org.springframework.http.HttpStatus;

public class GatewayTimeoutException extends Exception{

    @Override
    public String getMessage() {
        return "Third party API returned a timeout error. Please try again.";
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.GATEWAY_TIMEOUT;
    }
}
