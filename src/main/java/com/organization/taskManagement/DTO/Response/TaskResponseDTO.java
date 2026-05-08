package com.organization.taskManagement.DTO.Response;
import com.organization.taskManagement.Enums.TaskStatus;
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
}

