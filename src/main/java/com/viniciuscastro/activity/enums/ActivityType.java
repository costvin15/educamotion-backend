package com.viniciuscastro.activity.enums;

public enum ActivityType {
    poll("POLL");

    private final String value;

    ActivityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
