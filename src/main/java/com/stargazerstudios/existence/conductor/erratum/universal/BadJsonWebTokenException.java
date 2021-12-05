package com.stargazerstudios.existence.conductor.erratum.universal;

import org.springframework.http.HttpStatus;

public class BadJsonWebTokenException extends Exception{

    @Override
    public String getMessage() {
        return "Error with the JSON Web Token. Please go through user authentication again.";
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_ACCEPTABLE;
    }
}
