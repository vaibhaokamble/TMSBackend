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

    public ProjectModel createProject(ProjectRequestDTO request) {
        ProjectModel project = ProjectModel.builder()
                .name(request.getName())
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .build();
        return projectRepository.save(project);
    }

    public List<ProjectModel> getAllProjects() {
        return projectRepository.findAll();
    }
}
