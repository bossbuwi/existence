package com.stargazerstudios.existence.conductor.constants;

public enum EnumHttpRequestMethod {
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    CONNECT("CONNECT"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE"),
    PATCH("PATCH");

    private final String value;

    EnumHttpRequestMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
