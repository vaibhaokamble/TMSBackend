package com.organization.taskManagement.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Priority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

    @JsonCreator
    public static Priority fromString(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toUpperCase().replace(" ", "_").replace("-", "_");
        for (Priority priority : Priority.values()) {
            if (priority.name().equals(normalized)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Invalid Priority: " + value);
    }
}
