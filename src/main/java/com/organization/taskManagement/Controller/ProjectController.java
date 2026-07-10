package com.organization.taskManagement.Controller;

import com.organization.taskManagement.DTO.Request.ProjectRequestDTO;
import com.organization.taskManagement.Model.ProjectModel;
import com.organization.taskManagement.Services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectModel> createProject(@RequestBody ProjectRequestDTO request) {
        return ResponseEntity.ok(projectService.createProject(request));
    }

    @GetMapping
    public ResponseEntity<List<ProjectModel>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }
}
