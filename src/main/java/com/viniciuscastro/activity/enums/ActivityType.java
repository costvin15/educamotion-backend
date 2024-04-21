package com.viniciuscastro.activity.enums;

public enum ActivityType {
    poll("POLL"),
    free_answer("FREE_ANSWER");

    private final String value;

    ActivityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
