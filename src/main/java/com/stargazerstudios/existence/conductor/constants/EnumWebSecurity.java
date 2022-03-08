package com.stargazerstudios.existence.conductor.constants;

public enum EnumWebSecurity {
    HEADER_STRING("Authorization"),
    TOKEN_PREFIX("Bearer "),
    AUTHORITY_CLAIMS("authorities");

    private final String value;

    EnumWebSecurity(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
