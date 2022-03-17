package com.stargazerstudios.existence.conductor.constants;

public enum EnumRank {
    ROLE_OWNER(0),
    ROLE_SUPERUSER(1),
    ROLE_ADMIN(2),
    ROLE_USER(3),
    ROLE_BANNED(1000);

    private final long value;

    EnumRank(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
