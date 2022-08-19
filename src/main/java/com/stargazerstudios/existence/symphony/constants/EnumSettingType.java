package com.stargazerstudios.existence.symphony.constants;

public enum EnumSettingType {
    BACKEND_FEATURE("B"),
    FRONTEND_FEATURE("F"),
    SWITCHABLE_FEATURE("SW");

    private final String value;

    EnumSettingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
