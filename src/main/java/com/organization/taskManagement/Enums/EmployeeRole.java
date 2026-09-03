package com.organization.taskManagement.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EmployeeRole {
    TEAM_LEAD,
    EMPLOYEE;

    @JsonCreator
    public static EmployeeRole fromString(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toUpperCase().replace(" ", "_").replace("-", "_");
        for (EmployeeRole role : EmployeeRole.values()) {
            if (role.name().equals(normalized)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid EmployeeRole: " + value);
    }
}
