package com.organization.taskManagement.DTO.Request;

import com.organization.taskManagement.Enums.TaskStatus;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {
    private String title;
    private String description;
    private String teamId;
    private String assignedToId;
    private LocalDate dueDate;
    private TaskStatus status;
    private LocalDate updatedAt;
}
