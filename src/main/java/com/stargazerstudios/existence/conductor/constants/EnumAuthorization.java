package com.stargazerstudios.existence.conductor.constants;

public enum EnumAuthorization {
    DEFAULT_OWNER("admin"),
    DEFAULT_USER("user01"),
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
