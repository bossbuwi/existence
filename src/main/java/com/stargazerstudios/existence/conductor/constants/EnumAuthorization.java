package com.stargazerstudios.existence.conductor.constants;

public enum EnumAuthorization {
    DEFAULT_OWNER("owner"),
    DEFAULT_USER("user"),
    OWNER("ROLE_OWNER"),
    SUPERUSER("ROLE_SUPERUSER"),
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    BANNED("ROLE_BANNED");

    private final String value;

    EnumAuthorization(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
