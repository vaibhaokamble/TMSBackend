package com.organization.taskManagement.Controller;

import com.organization.taskManagement.DTO.Request.TeamRequestDTO;
import com.organization.taskManagement.Model.TeamModel;
import com.organization.taskManagement.Services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamModel> createTeam(@RequestBody TeamRequestDTO request) {
        return ResponseEntity.ok(teamService.createTeam(request));
    }

    @PostMapping("/{teamId}/assign-project/{projectId}")
    public ResponseEntity<TeamModel> assignTeamToProject(@PathVariable Long teamId, @PathVariable Long projectId) {
        return ResponseEntity.ok(teamService.assignTeamToProject(teamId, projectId));
    }

    @GetMapping
    public ResponseEntity<List<TeamModel>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamModel> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamModel> updateTeam(@PathVariable Long id, @RequestBody TeamRequestDTO request) {
        return ResponseEntity.ok(teamService.updateTeam(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.ok().build();
    }
}
