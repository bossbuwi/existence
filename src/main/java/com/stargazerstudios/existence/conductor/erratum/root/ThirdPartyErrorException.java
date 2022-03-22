package com.stargazerstudios.existence.conductor.erratum.root;

import org.springframework.http.HttpStatus;

public class ThirdPartyErrorException extends Exception {

    public ThirdPartyErrorException (String message) {
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_GATEWAY;
    }
}
