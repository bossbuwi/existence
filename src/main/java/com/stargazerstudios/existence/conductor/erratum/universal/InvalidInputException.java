package com.stargazerstudios.existence.conductor.erratum.universal;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends UnexpectedInputException {

    public InvalidInputException(String key) {
        super("Key: " + key + " - not found on the input or contains invalid values. " +
                "Please restructure the request and try again.");
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
