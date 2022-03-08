package com.stargazerstudios.existence.conductor.constants;

public enum EnumHttpHeader {
    AUTHORIZATION("Authorization"),
    CACHE_CONTROL("Cache-Control"),
    CONTENT_DISPOSITION("Content-Disposition"),
    CONTENT_TYPE("Content-Type");

    private final String value;

    EnumHttpHeader(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
