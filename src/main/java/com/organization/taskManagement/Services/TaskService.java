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

    public TaskResponseDTO createTask(TaskRequestDTO taskRequest) {
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

        TaskModel task = TaskMapper.toEntity(taskRequest, employee, team, project);
        TaskModel savedTask = taskRepo.save(task);

        return TaskMapper.toResponse(savedTask);
    }

    public TaskResponseDTO getTaskById(Long id) {
        TaskModel task = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
        return TaskMapper.toResponse(task);
    }

    public List<TaskResponseDTO> getAllTasks() {
        return taskRepo.findAll().stream()
                .map(TaskMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequest) {
        TaskModel task = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));

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

        TaskMapper.updateEntity(task, taskRequest, employee, team, project);
        TaskModel updatedTask = taskRepo.save(task);

        return TaskMapper.toResponse(updatedTask);
    }

    public void deleteTask(Long id) {
        if (!taskRepo.existsById(id)) {
            throw new RuntimeException("Task not found with ID: " + id);
        }
        taskRepo.deleteById(id);
    }
}
