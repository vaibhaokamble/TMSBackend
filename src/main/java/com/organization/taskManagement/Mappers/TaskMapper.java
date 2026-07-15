package com.organization.taskManagement.Mappers;

import com.organization.taskManagement.DTO.Request.TaskRequestDTO;
import com.organization.taskManagement.DTO.Response.TaskResponseDTO;
import com.organization.taskManagement.Enums.TaskStatus;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Model.ProjectModel;
import com.organization.taskManagement.Model.TaskModel;
import com.organization.taskManagement.Model.TeamModel;

public class TaskMapper {

    public static TaskModel toEntity(TaskRequestDTO request, EmployeeRegisterModel employee, TeamModel team, ProjectModel project, EmployeeRegisterModel createdBy) {

        if (request == null) return null;

        TaskStatus status = request.getStatus();
        if (status == null || status == TaskStatus.NEW || status == TaskStatus.ASSIGNED) {
            status = (employee != null) ? TaskStatus.ASSIGNED : TaskStatus.NEW;
        }

        return TaskModel.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .assignedTo(employee)
                .assignedTeam(team)
                .project(project)
                .status(status)
                .priority(request.getPriority())
                .createdBy(createdBy)
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
                .assignedTeamId(task.getAssignedTeam() != null ? task.getAssignedTeam().getId() : null)
                .projectId(task.getProject() != null ? task.getProject().getId() : null)
                .priority(task.getPriority())
                .createdById(task.getCreatedBy() != null ? task.getCreatedBy().getEmployeeId() : null)
                .build();
    }

    public static void updateEntity(TaskModel task, TaskRequestDTO request, EmployeeRegisterModel employee, TeamModel team, ProjectModel project) {
        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (employee != null) task.setAssignedTo(employee);
        if (team != null) task.setAssignedTeam(team);
        if (project != null) task.setProject(project);
    }
}
