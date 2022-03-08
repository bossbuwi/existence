package com.stargazerstudios.existence.conductor.constants;

public class WebSecurityURI {

    public static class Unguarded {
        public static final String URI_LOGIN = "/concerto/login";
        public static final String URI_CONCERTO = "/conductor/**";
        public static final String URI_FRONTEND_ROOT = "/*";
        public static final String URI_TEST = "/conductor/erratum/**";
    }
}
