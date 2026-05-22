package com.organization.taskManagement.DTO.Response;

import com.organization.taskManagement.Enums.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String token;
    private String employeeId;
    private EmployeeRole role;
    private String name;
}
