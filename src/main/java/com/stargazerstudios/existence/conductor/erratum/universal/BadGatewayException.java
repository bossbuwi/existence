package com.stargazerstudios.existence.conductor.erratum.universal;

import org.springframework.http.HttpStatus;

public class BadGatewayException extends Exception{

    @Override
    public String getMessage() {
        return "Third party API returned an unknown error.";
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_GATEWAY;
    }
}
