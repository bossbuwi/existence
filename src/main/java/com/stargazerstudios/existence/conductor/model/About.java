package com.stargazerstudios.existence.conductor.model;

import java.util.HashMap;

public class About {
    public static String APP_NAME = "Existence";
    public static String NAMED_RELEASE = "alpha";
    public static String VERSION_NUMBER = "0.1";

    public static class Developer {
        public static String DEV_NAME = "bossbuwi";
    }

    public static class BackendStack {
        public static String DATABASE = "PostgreSQL 14";
        public static String FRAMEWORK = "Spring Boot 2.6.0";
        public static String LANGUAGE = "Java 8";
    }

    public static class Universe {
        public static String[] CONSTELLATIONS = {
                "Home",
                "Sonata",
                "Ballad"
        };
    }
}
