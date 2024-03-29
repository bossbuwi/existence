package com.stargazerstudios.existence.conductor.erratum.system;

import com.stargazerstudios.existence.conductor.erratum.root.SystemException;
import org.springframework.http.HttpStatus;

public class InvalidSettingException extends SystemException {

    public InvalidSettingException(String key) {
        super("The value provided for setting with key: " + key + " is not valid.");
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
