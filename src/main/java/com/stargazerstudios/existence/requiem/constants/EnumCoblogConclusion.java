package com.stargazerstudios.existence.requiem.constants;

public enum EnumCoblogConclusion {
    CANCELLED("CANCELLED"),
    FULLCOB("FULLCOB"),
    ONGOING("ONGOING"),
    REOPENED("REOPENED");

    private final String value;

    EnumCoblogConclusion(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
