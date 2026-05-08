package com.organization.taskManagement.Controller;

import com.organization.taskManagement.DTO.Response.ApiResponseDTO;
import com.organization.taskManagement.DTO.Request.TaskRequestDTO;
import com.organization.taskManagement.DTO.Response.TaskResponseDTO;
import com.organization.taskManagement.Services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/task")
    public ResponseEntity<ApiResponseDTO<TaskResponseDTO>> createTask(@RequestBody TaskRequestDTO taskRequest) {
        TaskResponseDTO taskResponse = taskService.createTask(taskRequest);
        return ResponseEntity.ok(ApiResponseDTO.success("Task created successfully", taskResponse));
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<ApiResponseDTO<TaskResponseDTO>> getTaskById(@PathVariable Long id) {
        TaskResponseDTO taskResponse = taskService.getTaskById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Task retrieved successfully", taskResponse));
    }

    @GetMapping("/tasks")
    public ResponseEntity<ApiResponseDTO<List<TaskResponseDTO>>> getAllTasks() {
        List<TaskResponseDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(ApiResponseDTO.success("Tasks retrieved successfully", tasks));
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<ApiResponseDTO<TaskResponseDTO>> updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO taskRequest) {
        TaskResponseDTO taskResponse = taskService.updateTask(id, taskRequest);
        return ResponseEntity.ok(ApiResponseDTO.success("Task updated successfully", taskResponse));
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<ApiResponseDTO<String>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Task deleted successfully", null));
    }
}
