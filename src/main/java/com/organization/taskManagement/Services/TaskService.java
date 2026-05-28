package com.organization.taskManagement.Services;

import com.organization.taskManagement.DTO.Request.TaskRequestDTO;
import com.organization.taskManagement.DTO.Response.TaskResponseDTO;
import com.organization.taskManagement.Mappers.TaskMapper;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Model.TaskModel;
import com.organization.taskManagement.Repository.EmployeeRegisterRepository;
import com.organization.taskManagement.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.organization.taskManagement.Exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepo;
    private final EmployeeRegisterRepository employeeRegRepo;

    // create task service
    public TaskResponseDTO createTask(TaskRequestDTO taskRequest) {
        EmployeeRegisterModel employee = null;
        if (taskRequest.getAssignedToId() != null && !taskRequest.getAssignedToId().isEmpty()) {
            employee = employeeRegRepo.findByEmployeeId(taskRequest.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", taskRequest.getAssignedToId()));
        }

        TaskModel task = TaskMapper.toEntity(taskRequest, employee);
        TaskModel savedTask = taskRepo.save(task);

        return TaskMapper.toResponse(savedTask);
    }

    public TaskResponseDTO getTaskById(Long id) {
        TaskModel task = taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
        return TaskMapper.toResponse(task);
    }

    public List<TaskResponseDTO> getAllTasks() {
        return taskRepo.findAll().stream()
                .map(TaskMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequest) {
        TaskModel task = taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));

        EmployeeRegisterModel employee = null;
        if (taskRequest.getAssignedToId() != null && !taskRequest.getAssignedToId().isEmpty()) {
            employee = employeeRegRepo.findByEmployeeId(taskRequest.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "employeeId", taskRequest.getAssignedToId()));
        }

        TaskMapper.updateEntity(task, taskRequest, employee);
        TaskModel updatedTask = taskRepo.save(task);

        return TaskMapper.toResponse(updatedTask);
    }

    public void deleteTask(Long id) {
        if (!taskRepo.existsById(id)) {
            throw new ResourceNotFoundException("Task", id);
        }
        taskRepo.deleteById(id);
    }
}
