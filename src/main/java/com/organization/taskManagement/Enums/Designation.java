package com.organization.taskManagement.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Designation {

    JAVA_DEVELOPER,
    PYTHON_DEVELOPER,
    DEVOPS_ENGINEER,
    TEST_ENGINEER,
    DATA_ANALYST,
    HR,
    BUSINESS_ANALYST,
    PROJECT_MANAGER,
    FULL_STACK_DEVELOPER;

    @JsonCreator
    public static Designation fromString(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toUpperCase().replace(" ", "_").replace("-", "_");
        for (Designation designation : Designation.values()) {
            if (designation.name().equals(normalized)) {
                return designation;
            }
        }
        throw new IllegalArgumentException("Invalid Designation: " + value);
    }
}