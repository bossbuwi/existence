package com.stargazerstudios.existence.conductor.model;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter @Setter
public class ExistenceIdentity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String version;
    private String release;
    private String[] constellations;

    public ExistenceIdentity() {
        setName(About.APP_NAME);
        setVersion(About.VERSION_NUMBER);
        setRelease(About.NAMED_RELEASE);
        setConstellations(About.Universe.CONSTELLATIONS);
    }
}
