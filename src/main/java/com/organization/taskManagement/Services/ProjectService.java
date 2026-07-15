package com.organization.taskManagement.Services;

import com.organization.taskManagement.DTO.Request.ProjectRequestDTO;
import com.organization.taskManagement.Model.ProjectModel;
import com.organization.taskManagement.Repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final com.organization.taskManagement.Repository.EmployeeRegisterRepository employeeRegRepo;
    private final ActivityLogService activityLogService;

    public ProjectModel createProject(ProjectRequestDTO request) {
        ProjectModel project = ProjectModel.builder()
                .name(request.getName())
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .build();
        ProjectModel savedProject = projectRepository.save(project);
        activityLogService.logActivity("Project Created", "Project created: " + savedProject.getName());
        return savedProject;
    }

    public List<ProjectModel> getAllProjects(com.organization.taskManagement.security.UserInfoDetails userDetails) {
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("TEAM_LEAD"))) {
            return projectRepository.findAll();
        }

        com.organization.taskManagement.Model.EmployeeRegisterModel employee = employeeRegRepo.findByEmployeeId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return employee.getTeams().stream()
                .flatMap(team -> team.getProjects().stream())
                .distinct()
                .toList();
    }

    public ProjectModel getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public ProjectModel updateProject(Long id, ProjectRequestDTO request) {
        ProjectModel project = getProjectById(id);
        if (request.getName() != null) project.setName(request.getName());
        if (request.getDescription() != null) project.setDescription(request.getDescription());
        if (request.getDeadline() != null) project.setDeadline(request.getDeadline());
        ProjectModel updatedProject = projectRepository.save(project);
        activityLogService.logActivity("Project Updated", "Project updated: " + updatedProject.getName());
        return updatedProject;
    }

    public void deleteProject(Long id) {
        ProjectModel project = getProjectById(id);
        String name = project.getName();
        projectRepository.delete(project);
        activityLogService.logActivity("Project Deleted", "Project deleted: " + name);
    }
}
