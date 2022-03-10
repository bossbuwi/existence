package com.stargazerstudios.existence.conductor.constants;

public enum EnumUtilOutput {
    INVALID("OUTPUT_INVALID"),
    EMPTY("OUTPUT_EMPTY");

    private final String value;

    EnumUtilOutput(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
