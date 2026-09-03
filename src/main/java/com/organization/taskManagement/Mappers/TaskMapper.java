package com.organization.taskManagement.Mappers;

import com.organization.taskManagement.DTO.Request.TaskRequestDTO;
import com.organization.taskManagement.DTO.Response.TaskResponseDTO;
import com.organization.taskManagement.Enums.TaskStatus;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Model.ProjectModel;
import com.organization.taskManagement.Model.TaskModel;
import com.organization.taskManagement.Model.TeamModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {

    @Mapping(source = "assignedTo.employeeId", target = "assignedToId")
    @Mapping(source = "assignedTeam.id", target = "assignedTeamId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "createdBy.employeeId", target = "createdById")
    TaskResponseDTO toResponse(TaskModel task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "request.title", target = "title")
    @Mapping(source = "request.description", target = "description")
    @Mapping(source = "request.dueDate", target = "dueDate")
    @Mapping(source = "request.priority", target = "priority")
    @Mapping(source = "employee", target = "assignedTo")
    @Mapping(source = "team", target = "assignedTeam")
    @Mapping(source = "project", target = "project")
    @Mapping(source = "createdBy", target = "createdBy")
    TaskModel toEntity(TaskRequestDTO request, EmployeeRegisterModel employee, TeamModel team, ProjectModel project, EmployeeRegisterModel createdBy);

    @AfterMapping
    default void setStatusForNewTask(@MappingTarget TaskModel task, TaskRequestDTO request, EmployeeRegisterModel employee) {
        if (request == null) return;
        TaskStatus status = request.getStatus();
        if (status == null || status == TaskStatus.NEW || status == TaskStatus.ASSIGNED) {
            task.setStatus(employee != null ? TaskStatus.ASSIGNED : TaskStatus.NEW);
        } else {
            task.setStatus(status);
        }
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "request.title", target = "title")
    @Mapping(source = "request.description", target = "description")
    @Mapping(source = "request.dueDate", target = "dueDate")
    @Mapping(source = "request.priority", target = "priority")
    @Mapping(source = "request.status", target = "status")
    @Mapping(source = "employee", target = "assignedTo")
    @Mapping(source = "team", target = "assignedTeam")
    @Mapping(source = "project", target = "project")
    void updateEntity(@MappingTarget TaskModel task, TaskRequestDTO request, EmployeeRegisterModel employee, TeamModel team, ProjectModel project);
}
