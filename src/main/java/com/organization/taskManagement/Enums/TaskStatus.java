package com.organization.taskManagement.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TaskStatus {
    NEW,
    ASSIGNED,
    IN_PROGRESS,
    ON_HOLD,
    IN_REVIEW,
    DONE;

    @JsonCreator
    public static TaskStatus fromString(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toUpperCase().replace(" ", "_").replace("-", "_");
        for (TaskStatus status : TaskStatus.values()) {
            if (status.name().equals(normalized)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid TaskStatus: " + value);
    }
}

