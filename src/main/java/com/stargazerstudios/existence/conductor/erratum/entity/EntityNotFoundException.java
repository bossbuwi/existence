package com.stargazerstudios.existence.conductor.erratum.entity;

import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends EntityException {

    public EntityNotFoundException(String entityName, String key, String value) {
        super(entityName + " with " + key + ": " + value + " not found.");
    }

    public EntityNotFoundException(String entityName1, String key1, String value1,
                                   String entityName2, String key2, String value2) {
        super(entityName1 + " with " + key1 + ": " + value1 + " not found on "
                + entityName2 + " with " + key2 + ": " + value2);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
