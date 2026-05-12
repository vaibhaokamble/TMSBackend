package com.organization.taskManagement.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {
    public String id;
    public String message;
    public String employeeId;
    public String name;
    private String timestamp;
}
