package com.organization.taskManagement.DTO.Response;
import com.organization.taskManagement.Enums.TaskStatus;
import com.organization.taskManagement.Enums.Priority;
import lombok.*;

@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private java.time.LocalDate dueDate;
    private String assignedToId;
    private Long assignedTeamId;
    private Long projectId;
    private Priority priority;
    private String createdById;
}
