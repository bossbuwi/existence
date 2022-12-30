package com.stargazerstudios.existence.conductor.erratum.root;

import org.springframework.http.HttpStatus;

public class ThirdPartyException extends Exception {

    public ThirdPartyException(String message) {
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_GATEWAY;
    }
}
