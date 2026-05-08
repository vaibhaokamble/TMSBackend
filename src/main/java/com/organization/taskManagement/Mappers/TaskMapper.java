package com.organization.taskManagement.Mappers;

import com.organization.taskManagement.DTO.Request.TaskRequestDTO;
import com.organization.taskManagement.DTO.Response.TaskResponseDTO;
import com.organization.taskManagement.Enums.TaskStatus;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Model.TaskModel;

public class TaskMapper {

    public static TaskModel toEntity(TaskRequestDTO request, EmployeeRegisterModel employee) {

        if (request == null) return null;

        TaskStatus status = request.getStatus();
        if (status == null) {
            status = (employee != null) ? TaskStatus.ASSIGN : TaskStatus.NEW;
        }

        return TaskModel.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .assignedTo(employee)
                .status(status)
                .build();
    }

    public static TaskResponseDTO toResponse(TaskModel task) {

        if (task == null) return null;

        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .assignedToId(task.getAssignedTo() != null ? task.getAssignedTo().getEmployeeId() : null)
                .build();
    }

    public static void updateEntity(TaskModel task, TaskRequestDTO request, EmployeeRegisterModel employee) {
        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
        if (employee != null) task.setAssignedTo(employee);
    }
}
