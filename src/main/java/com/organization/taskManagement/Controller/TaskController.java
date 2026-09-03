package com.organization.taskManagement.Controller;

import com.organization.taskManagement.DTO.Request.TaskRequestDTO;
import com.organization.taskManagement.DTO.Response.ApiResponse;
import com.organization.taskManagement.DTO.Response.TaskResponseDTO;
import com.organization.taskManagement.Services.TaskService;
import com.organization.taskManagement.security.CurrentUser;
import com.organization.taskManagement.security.UserInfoDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller handling task-related operations.
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * Creates a new task.
     * @param taskRequest the task creation request payload
     * @param userDetails the currently authenticated user
     * @return the created task wrapped in an ApiResponse
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponseDTO>> createTask(
            @RequestBody TaskRequestDTO taskRequest,
            @CurrentUser UserInfoDetails userDetails) {
        TaskResponseDTO response = taskService.createTask(taskRequest, userDetails);
        return ResponseEntity.ok(ApiResponse.success("Task created successfully", response));
    }

    /**
     * Retrieves a task by its ID.
     * @param id the task ID
     * @return the task details wrapped in an ApiResponse
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> getTaskById(@PathVariable Long id) {
        TaskResponseDTO response = taskService.getTaskById(id);
        return ResponseEntity.ok(ApiResponse.success("Task retrieved successfully", response));
    }

    /**
     * Retrieves all tasks based on the user's role.
     * @param userDetails the currently authenticated user
     * @return a list of tasks wrapped in an ApiResponse
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> getAllTasks(
            @CurrentUser UserInfoDetails userDetails) {
        List<TaskResponseDTO> response = taskService.getAllTasks(userDetails);
        return ResponseEntity.ok(ApiResponse.success("Tasks retrieved successfully", response));
    }

    /**
     * Retrieves tasks associated with a specific project.
     * @param projectId the project ID
     * @return a list of tasks wrapped in an ApiResponse
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> getTasksByProjectId(@PathVariable Long projectId) {
        List<TaskResponseDTO> response = taskService.getTasksByProjectId(projectId);
        return ResponseEntity.ok(ApiResponse.success("Project tasks retrieved successfully", response));
    }

    /**
     * Retrieves tasks assigned to a specific employee.
     * @param employeeId the employee ID
     * @return a list of tasks wrapped in an ApiResponse
     */
    @GetMapping("/assignee/{employeeId}")
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> getTasksByAssigneeId(@PathVariable String employeeId) {
        List<TaskResponseDTO> response = taskService.getTasksByAssigneeId(employeeId);
        return ResponseEntity.ok(ApiResponse.success("Assignee tasks retrieved successfully", response));
    }

    /**
     * Updates an existing task.
     * @param id the task ID to update
     * @param taskRequest the update payload
     * @param userDetails the currently authenticated user
     * @return the updated task wrapped in an ApiResponse
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> updateTask(
            @PathVariable Long id,
            @RequestBody TaskRequestDTO taskRequest,
            @CurrentUser UserInfoDetails userDetails) {
        TaskResponseDTO response = taskService.updateTask(id, taskRequest, userDetails);
        return ResponseEntity.ok(ApiResponse.success("Task updated successfully", response));
    }

    /**
     * Deletes a task.
     * @param id the task ID to delete
     * @param userDetails the currently authenticated user
     * @return an ApiResponse indicating success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @PathVariable Long id,
            @CurrentUser UserInfoDetails userDetails) {
        taskService.deleteTask(id, userDetails);
        return ResponseEntity.ok(ApiResponse.success("Task deleted successfully"));
    }
}
