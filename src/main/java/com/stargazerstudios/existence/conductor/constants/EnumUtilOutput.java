package com.stargazerstudios.existence.conductor.constants;

public enum EnumUtilOutput {
    INVALID("3b50c9c1-1bff-4cea-89a9-e94bbd15ece3"),
    EMPTY("444fde17-8fa0-4f3c-9cbb-201291439e6d");

    private final String value;

    EnumUtilOutput(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
