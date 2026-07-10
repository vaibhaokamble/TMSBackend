package com.organization.taskManagement.Services;

import com.organization.taskManagement.DTO.Request.TeamRequestDTO;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Model.ProjectModel;
import com.organization.taskManagement.Model.TeamModel;
import com.organization.taskManagement.Repository.EmployeeRegisterRepository;
import com.organization.taskManagement.Repository.ProjectRepository;
import com.organization.taskManagement.Repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final EmployeeRegisterRepository employeeRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public TeamModel createTeam(TeamRequestDTO request) {
        List<EmployeeRegisterModel> members = new ArrayList<>();
        if (request.getMemberIds() != null && !request.getMemberIds().isEmpty()) {
            members = employeeRepository.findAllById(request.getMemberIds());
        }
        TeamModel team = TeamModel.builder()
                .name(request.getName())
                .members(members)
                .build();
        return teamRepository.save(team);
    }

    @Transactional
    public TeamModel assignTeamToProject(Long teamId, Long projectId) {
        TeamModel team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
        ProjectModel project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        
        if (!team.getProjects().contains(project)) {
            team.getProjects().add(project);
        }
        if (!project.getTeams().contains(team)) {
            project.getTeams().add(team);
        }
        
        projectRepository.save(project);
        return teamRepository.save(team);
    }

    public List<TeamModel> getAllTeams() {
        return teamRepository.findAll();
    }
}
