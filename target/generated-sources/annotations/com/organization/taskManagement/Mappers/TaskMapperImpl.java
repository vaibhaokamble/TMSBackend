package com.organization.taskManagement.Mappers;

import com.organization.taskManagement.DTO.Request.TaskRequestDTO;
import com.organization.taskManagement.DTO.Response.TaskResponseDTO;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Model.ProjectModel;
import com.organization.taskManagement.Model.TaskModel;
import com.organization.taskManagement.Model.TeamModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-07T14:45:05+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Oracle Corporation)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public TaskResponseDTO toResponse(TaskModel task) {
        if ( task == null ) {
            return null;
        }

        TaskResponseDTO.TaskResponseDTOBuilder taskResponseDTO = TaskResponseDTO.builder();

        taskResponseDTO.assignedToId( taskAssignedToEmployeeId( task ) );
        taskResponseDTO.assignedTeamId( taskAssignedTeamId( task ) );
        taskResponseDTO.projectId( taskProjectId( task ) );
        taskResponseDTO.createdById( taskCreatedByEmployeeId( task ) );
        taskResponseDTO.id( task.getId() );
        taskResponseDTO.title( task.getTitle() );
        taskResponseDTO.description( task.getDescription() );
        taskResponseDTO.status( task.getStatus() );
        taskResponseDTO.dueDate( task.getDueDate() );
        taskResponseDTO.priority( task.getPriority() );

        return taskResponseDTO.build();
    }

    @Override
    public TaskModel toEntity(TaskRequestDTO request, EmployeeRegisterModel employee, TeamModel team, ProjectModel project, EmployeeRegisterModel createdBy) {
        if ( request == null && employee == null && team == null && project == null && createdBy == null ) {
            return null;
        }

        TaskModel.TaskModelBuilder taskModel = TaskModel.builder();

        if ( request != null ) {
            taskModel.title( request.getTitle() );
            taskModel.description( request.getDescription() );
            taskModel.dueDate( request.getDueDate() );
            taskModel.priority( request.getPriority() );
            taskModel.status( request.getStatus() );
        }
        taskModel.assignedTo( employee );
        taskModel.assignedTeam( team );
        taskModel.project( project );
        taskModel.createdBy( createdBy );

        return taskModel.build();
    }

    @Override
    public void updateEntity(TaskModel task, TaskRequestDTO request, EmployeeRegisterModel employee, TeamModel team, ProjectModel project) {
        if ( request == null && employee == null && team == null && project == null ) {
            return;
        }

        if ( request != null ) {
            if ( request.getTitle() != null ) {
                task.setTitle( request.getTitle() );
            }
            if ( request.getDescription() != null ) {
                task.setDescription( request.getDescription() );
            }
            if ( request.getDueDate() != null ) {
                task.setDueDate( request.getDueDate() );
            }
            if ( request.getPriority() != null ) {
                task.setPriority( request.getPriority() );
            }
            if ( request.getStatus() != null ) {
                task.setStatus( request.getStatus() );
            }
        }
        if ( employee != null ) {
            task.setAssignedTo( employee );
        }
        if ( team != null ) {
            task.setAssignedTeam( team );
        }
        if ( project != null ) {
            task.setProject( project );
        }

        setStatusForNewTask( task, request, employee );
    }

    private String taskAssignedToEmployeeId(TaskModel taskModel) {
        if ( taskModel == null ) {
            return null;
        }
        EmployeeRegisterModel assignedTo = taskModel.getAssignedTo();
        if ( assignedTo == null ) {
            return null;
        }
        String employeeId = assignedTo.getEmployeeId();
        if ( employeeId == null ) {
            return null;
        }
        return employeeId;
    }

    private Long taskAssignedTeamId(TaskModel taskModel) {
        if ( taskModel == null ) {
            return null;
        }
        TeamModel assignedTeam = taskModel.getAssignedTeam();
        if ( assignedTeam == null ) {
            return null;
        }
        Long id = assignedTeam.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long taskProjectId(TaskModel taskModel) {
        if ( taskModel == null ) {
            return null;
        }
        ProjectModel project = taskModel.getProject();
        if ( project == null ) {
            return null;
        }
        Long id = project.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String taskCreatedByEmployeeId(TaskModel taskModel) {
        if ( taskModel == null ) {
            return null;
        }
        EmployeeRegisterModel createdBy = taskModel.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        String employeeId = createdBy.getEmployeeId();
        if ( employeeId == null ) {
            return null;
        }
        return employeeId;
    }
}
