package com.stargazerstudios.existence.conductor.erratum.root;

import org.springframework.http.HttpStatus;

public class FileProcessingException extends Exception {

    public FileProcessingException(String message) {
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.INSUFFICIENT_STORAGE;
    }
}
