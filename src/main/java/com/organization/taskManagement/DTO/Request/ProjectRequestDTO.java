package com.organization.taskManagement.DTO.Request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProjectRequestDTO {
    private String name;
    private String description;
    private LocalDateTime deadline;
}
