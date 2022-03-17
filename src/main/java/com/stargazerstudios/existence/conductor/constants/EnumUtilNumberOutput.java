package com.stargazerstudios.existence.conductor.constants;

public enum EnumUtilNumberOutput {
    INVALID(20292),
    EMPTY(113091);

    private final int value;

    EnumUtilNumberOutput(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
