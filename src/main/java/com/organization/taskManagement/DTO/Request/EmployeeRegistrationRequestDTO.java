package com.organization.taskManagement.DTO.Request;

import com.organization.taskManagement.Enums.EmployeeRole;
import com.organization.taskManagement.Enums.Designation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRegistrationRequestDTO {
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[A-Z][a-z]+\\s[A-Z][a-z]+$", message = "Name must be in format 'Firstname Lastname'")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@koderztech\\.com$", message = "Email must be a valid @koderztech.com address")
    private String email;

    @NotBlank(message = "Employee ID is required")
    @Pattern(regexp = "^KDZ[0-9]+$", message = "Employee ID must start with 'KDZ' followed by numbers")
    private String employeeId;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
            message = "Password must contain at least one digit, one lowercase, one uppercase letter, and one special character")
    private String password;

    @NotNull(message = "Role is required")
    private EmployeeRole role;

    @NotNull(message = "Designation is required")
    private Designation designation;
}
