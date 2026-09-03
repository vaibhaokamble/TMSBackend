package com.organization.taskManagement.Services;

import com.organization.taskManagement.DTO.Request.TaskRequestDTO;
import com.organization.taskManagement.DTO.Response.TaskResponseDTO;
import com.organization.taskManagement.Mappers.TaskMapper;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Model.ProjectModel;
import com.organization.taskManagement.Model.TaskModel;
import com.organization.taskManagement.Model.TeamModel;
import com.organization.taskManagement.Repository.EmployeeRegisterRepository;
import com.organization.taskManagement.Repository.ProjectRepository;
import com.organization.taskManagement.Repository.TaskRepository;
import com.organization.taskManagement.Repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepo;
    private final EmployeeRegisterRepository employeeRegRepo;
    private final TeamRepository teamRepo;
    private final ProjectRepository projectRepo;
    private final ActivityLogService activityLogService;
    private final TaskMapper taskMapper;

    public TaskResponseDTO createTask(TaskRequestDTO taskRequest, com.organization.taskManagement.security.UserInfoDetails userDetails) {
        boolean isTeamLead = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("TEAM_LEAD"));
        if (!isTeamLead) {
            throw new RuntimeException("Access Denied: Only TEAM_LEAD can create tasks.");
        }

        EmployeeRegisterModel createdBy = employeeRegRepo.findByEmployeeId(userDetails.getUsername())
                .orElse(null);
        EmployeeRegisterModel employee = null;
        if (taskRequest.getAssignedToId() != null && !taskRequest.getAssignedToId().isEmpty()) {
            employee = employeeRegRepo.findByEmployeeId(taskRequest.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + taskRequest.getAssignedToId()));
        }

        TeamModel team = null;
        if (taskRequest.getTeamId() != null) {
            team = teamRepo.findById(taskRequest.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found with ID: " + taskRequest.getTeamId()));
        }

        ProjectModel project = null;
        if (taskRequest.getProjectId() != null) {
            project = projectRepo.findById(taskRequest.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found with ID: " + taskRequest.getProjectId()));
        }

        TaskModel task = taskMapper.toEntity(taskRequest, employee, team, project, createdBy);
        TaskModel savedTask = taskRepo.save(task);

        if (employee != null) {
            activityLogService.logActivity("Task Created & Assigned", "Task created and assigned to Employee.", savedTask);
        } else {
            activityLogService.logActivity("Task Created", "Task created: " + savedTask.getTitle(), savedTask);
        }

        return taskMapper.toResponse(savedTask);
    }

    public TaskResponseDTO getTaskById(Long id) {
        TaskModel task = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
        return taskMapper.toResponse(task);
    }

    public List<TaskResponseDTO> getAllTasks(com.organization.taskManagement.security.UserInfoDetails userDetails) {
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("TEAM_LEAD"))) {
            return taskRepo.findAll().stream()
                    .map(taskMapper::toResponse)
                    .collect(Collectors.toList());
        }
        
        return taskRepo.findByAssignedTo_EmployeeId(userDetails.getUsername()).stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<TaskResponseDTO> getTasksByProjectId(Long projectId) {
        return taskRepo.findByProjectId(projectId).stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<TaskResponseDTO> getTasksByAssigneeId(String employeeId) {
        return taskRepo.findByAssignedTo_EmployeeId(employeeId).stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequest, com.organization.taskManagement.security.UserInfoDetails userDetails) {
        TaskModel task = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));

        boolean isEmployee = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("EMPLOYEE"));
        
        if (isEmployee) {
            if (task.getAssignedTo() == null || !task.getAssignedTo().getEmployeeId().equals(userDetails.getUsername())) {
                throw new RuntimeException("Access Denied: You can only update tasks assigned to you.");
            }
            
            if (taskRequest.getStatus() != null) {
                com.organization.taskManagement.Enums.TaskStatus oldStatus = task.getStatus();
                com.organization.taskManagement.Enums.TaskStatus newStatus = taskRequest.getStatus();
                
                validateStatusTransition(oldStatus, newStatus);
                
                task.setStatus(newStatus);
                TaskModel updatedTask = taskRepo.save(task);
                
                String logMessage = "Task status updated to " + newStatus;
                if (newStatus == com.organization.taskManagement.Enums.TaskStatus.IN_PROGRESS) {
                    logMessage = "Employee started working on Task.";
                } else if (newStatus == com.organization.taskManagement.Enums.TaskStatus.DONE) {
                    logMessage = "Employee completed Task.";
                }
                activityLogService.logActivity("Status Changed", logMessage, updatedTask);
                return taskMapper.toResponse(updatedTask);
            } else {
                throw new RuntimeException("Employees can only change task status.");
            }
        }

        EmployeeRegisterModel employee = null;
        if (taskRequest.getAssignedToId() != null && !taskRequest.getAssignedToId().isEmpty()) {
            employee = employeeRegRepo.findByEmployeeId(taskRequest.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + taskRequest.getAssignedToId()));
        }

        TeamModel team = null;
        if (taskRequest.getTeamId() != null) {
            team = teamRepo.findById(taskRequest.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found with ID: " + taskRequest.getTeamId()));
        }

        ProjectModel project = null;
        if (taskRequest.getProjectId() != null) {
            project = projectRepo.findById(taskRequest.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found with ID: " + taskRequest.getProjectId()));
        }

        boolean wasUnassigned = task.getAssignedTo() == null;
        boolean isNowAssigned = employee != null;

        if (taskRequest.getStatus() != null) {
            validateStatusTransition(task.getStatus(), taskRequest.getStatus());
        }

        taskMapper.updateEntity(task, taskRequest, employee, team, project);
        
        if (wasUnassigned && isNowAssigned) {
            task.setStatus(com.organization.taskManagement.Enums.TaskStatus.ASSIGNED);
        }
        
        TaskModel updatedTask = taskRepo.save(task);

        if (wasUnassigned && isNowAssigned) {
            activityLogService.logActivity("Task Assigned", "Task assigned to Employee.", updatedTask);
        } else {
            activityLogService.logActivity("Task Updated", "Task updated: " + updatedTask.getTitle(), updatedTask);
        }

        return taskMapper.toResponse(updatedTask);
    }
    
    private void validateStatusTransition(com.organization.taskManagement.Enums.TaskStatus oldStatus, com.organization.taskManagement.Enums.TaskStatus newStatus) {
        if (oldStatus == newStatus) return;
        
        boolean valid = false;
        switch (oldStatus) {
            case NEW:
                valid = newStatus == com.organization.taskManagement.Enums.TaskStatus.ASSIGNED;
                break;
            case ASSIGNED:
                valid = newStatus == com.organization.taskManagement.Enums.TaskStatus.IN_PROGRESS;
                break;
            case IN_PROGRESS:
                valid = newStatus == com.organization.taskManagement.Enums.TaskStatus.ON_HOLD || 
                        newStatus == com.organization.taskManagement.Enums.TaskStatus.IN_REVIEW || 
                        newStatus == com.organization.taskManagement.Enums.TaskStatus.DONE;
                break;
            case ON_HOLD:
                valid = newStatus == com.organization.taskManagement.Enums.TaskStatus.IN_PROGRESS;
                break;
            case IN_REVIEW:
                valid = newStatus == com.organization.taskManagement.Enums.TaskStatus.DONE || 
                        newStatus == com.organization.taskManagement.Enums.TaskStatus.IN_PROGRESS;
                break;
            case DONE:
                valid = false;
                break;
        }
        
        if (!valid) {
            throw new RuntimeException("Invalid status transition from " + oldStatus + " to " + newStatus);
        }
    }

    public void deleteTask(Long id, com.organization.taskManagement.security.UserInfoDetails userDetails) {
        boolean isTeamLead = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("TEAM_LEAD"));
        if (!isTeamLead) {
            throw new RuntimeException("Access Denied: Only TEAM_LEAD can delete tasks.");
        }
        TaskModel task = taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
        String title = task.getTitle();
        taskRepo.deleteById(id);
        activityLogService.logActivity("Task Deleted", "Task deleted: " + title);
    }
}
