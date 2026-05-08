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
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {


    private final TaskService taskService;

    //post mapping task controller
    @PostMapping("/task")
    public ResponseEntity<ApiResponseDTO<TaskResponseDTO>> createTask(@RequestBody TaskRequestDTO taskRequest) {
        try {
            TaskResponseDTO taskResponse = taskService.createTask(taskRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Task created successfully", taskResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.failure(e.getMessage()));
        }
    }

    // get mapping task api byId
    @GetMapping("/task/{id}")
    public ResponseEntity<ApiResponseDTO<TaskResponseDTO>> getTaskById(@PathVariable Long id) {
        try {
            TaskResponseDTO taskResponse = taskService.getTaskById(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Task retrieved successfully", taskResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.failure(e.getMessage()));
        }
    }

    // get all tasks
    @GetMapping("/tasks")
    public ResponseEntity<ApiResponseDTO<List<TaskResponseDTO>>> getAllTasks() {
        try {
            java.util.List<TaskResponseDTO> tasks = taskService.getAllTasks();
            return ResponseEntity.ok(ApiResponseDTO.success("Tasks retrieved successfully", tasks));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.failure(e.getMessage()));
        }
    }

    // update task
    @PutMapping("/task/{id}")
    public ResponseEntity<ApiResponseDTO<TaskResponseDTO>> updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO taskRequest) {
        try {
            TaskResponseDTO taskResponse = taskService.updateTask(id, taskRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Task updated successfully", taskResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.failure(e.getMessage()));
        }
    }

    // delete task
    @DeleteMapping("/task/{id}")
    public ResponseEntity<ApiResponseDTO<String>> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Task deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.failure(e.getMessage()));
        }
    }
}
