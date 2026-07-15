package com.organization.taskManagement.Controller;

import com.organization.taskManagement.DTO.Request.TaskRequestDTO;
import com.organization.taskManagement.DTO.Response.TaskResponseDTO;
import com.organization.taskManagement.Services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO taskRequest, @org.springframework.security.core.annotation.AuthenticationPrincipal com.organization.taskManagement.security.UserInfoDetails userDetails) {
        return ResponseEntity.ok(taskService.createTask(taskRequest, userDetails));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(@org.springframework.security.core.annotation.AuthenticationPrincipal com.organization.taskManagement.security.UserInfoDetails userDetails) {
        return ResponseEntity.ok(taskService.getAllTasks(userDetails));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getTasksByProjectId(projectId));
    }

    @GetMapping("/assignee/{employeeId}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByAssigneeId(@PathVariable String employeeId) {
        return ResponseEntity.ok(taskService.getTasksByAssigneeId(employeeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO taskRequest, @org.springframework.security.core.annotation.AuthenticationPrincipal com.organization.taskManagement.security.UserInfoDetails userDetails) {
        return ResponseEntity.ok(taskService.updateTask(id, taskRequest, userDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @org.springframework.security.core.annotation.AuthenticationPrincipal com.organization.taskManagement.security.UserInfoDetails userDetails) {
        taskService.deleteTask(id, userDetails);
        return ResponseEntity.ok().build();
    }
}
