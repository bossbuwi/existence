package com.stargazerstudios.existence.conductor.constants;

public class WebSecurityURI {

    public static class Unguarded {
        public static String URI_LOGIN = "/concerto/login";
        public static String URI_CONCERTO = "/conductor/**";
        public static String URI_FRONTEND_ROOT = "/*";
        public static String URI_TEST = "/conductor/erratum/**";
    }
}
