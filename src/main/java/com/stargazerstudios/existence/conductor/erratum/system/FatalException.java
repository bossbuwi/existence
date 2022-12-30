package com.stargazerstudios.existence.conductor.erratum.system;

import com.stargazerstudios.existence.conductor.erratum.root.SystemException;
import org.springframework.http.HttpStatus;

public class FatalException extends SystemException {

    public FatalException() {
        super("The application has encountered a fatal error. Please contact the admin.");
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
