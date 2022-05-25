package com.stargazerstudios.existence.conductor.model;

import com.stargazerstudios.existence.conductor.constants.AboutExistence;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter @Setter
public class ExistenceIdentity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String app_name;
    private String app_description;
    private String named_release;
    private String version_number;
    private String release_date;
    private String source_repository;
    private String developer;
    private String dev_site;
    private String database;
    private String backend_framework;
    private String backend_language;
    private String frontend_framework;
    private String frontend_language;
    private String frontend_theme;

    public ExistenceIdentity() {
        setApp_name(AboutExistence.APP_NAME);
        setApp_description(AboutExistence.APP_DESCRIPTION);
        setNamed_release(AboutExistence.NAMED_RELEASE);
        setVersion_number(AboutExistence.VERSION_NUMBER);
        setRelease_date(AboutExistence.RELEASE_DATE);
        setSource_repository(AboutExistence.SOURCE_REPOSITORY);
        setSource_repository(AboutExistence.SOURCE_REPOSITORY);
        setDeveloper(AboutExistence.Developer.DEV_NAME);
        setDev_site(AboutExistence.Developer.DEV_SITE);
        setDatabase(AboutExistence.BackendStack.DATABASE);
        setBackend_framework(AboutExistence.BackendStack.FRAMEWORK);
        setBackend_language(AboutExistence.BackendStack.LANGUAGE);
        setFrontend_framework(AboutExistence.FrontendStack.FRAMEWORK);
        setFrontend_language(AboutExistence.FrontendStack.LANGUAGE);
        setFrontend_theme(AboutExistence.FrontendStack.THEME);
    }
}
