package com.stargazerstudios.existence.conductor.model;

import lombok.*;

import java.io.Serializable;

@Getter @Setter
public class ExistenceIdentity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String version;
    private String release;

    public ExistenceIdentity() {
        setName(About.APP_NAME);
        setVersion(About.VERSION_NUMBER);
        setRelease(About.NAMED_RELEASE);
    }
}
