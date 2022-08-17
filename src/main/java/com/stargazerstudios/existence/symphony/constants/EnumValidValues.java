package com.stargazerstudios.existence.symphony.constants;

public enum EnumValidValues {
    ALPHA("ALPHA"),
    NUMERIC("NUMERIC"),
    ALPHANUMERIC("ALPHANUMERIC");

    private String value;

    EnumValidValues(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
