package com.stargazerstudios.existence.conductor.constants;

public enum SwitchableFeatures {
    RQM001("RQM001");

    private final String value;

    SwitchableFeatures(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
