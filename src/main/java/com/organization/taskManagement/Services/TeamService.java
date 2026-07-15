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
    private final ActivityLogService activityLogService;

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
        TeamModel savedTeam = teamRepository.save(team);
        activityLogService.logActivity("Team Created", "Team created: " + savedTeam.getName());
        return savedTeam;
    }

    @Transactional
    public TeamModel assignTeamToProject(Long teamId, Long projectId) {
        TeamModel team = getTeamById(teamId);
        ProjectModel project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        
        if (!team.getProjects().contains(project)) {
            team.getProjects().add(project);
        }
        if (!project.getTeams().contains(team)) {
            project.getTeams().add(team);
        }
        
        projectRepository.save(project);
        TeamModel savedTeam = teamRepository.save(team);
        activityLogService.logActivity("Project Assigned", "Project " + project.getName() + " assigned to team " + savedTeam.getName());
        return savedTeam;
    }

    public List<TeamModel> getAllTeams() {
        return teamRepository.findAll();
    }

    public TeamModel getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
    }

    @Transactional
    public TeamModel updateTeam(Long id, TeamRequestDTO request) {
        TeamModel team = getTeamById(id);
        if (request.getName() != null) team.setName(request.getName());
        
        if (request.getMemberIds() != null) {
            List<EmployeeRegisterModel> members = employeeRepository.findAllById(request.getMemberIds());
            team.setMembers(members);
        }
        TeamModel updatedTeam = teamRepository.save(team);
        activityLogService.logActivity("Team Updated", "Team updated: " + updatedTeam.getName());
        return updatedTeam;
    }

    @Transactional
    public void deleteTeam(Long id) {
        TeamModel team = getTeamById(id);
        String name = team.getName();
        teamRepository.delete(team);
        activityLogService.logActivity("Team Deleted", "Team deleted: " + name);
    }
}
