package com.organization.taskManagement.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsResponseDTO {
    private Long totalTasks;
    private Long completedTasks;
    private Long newTasks;
    private Long assignedTasks;
    private Long inProgressTasks;
    private Long overdueTasks;
    private Long todayAssignedTasks;
    private Long totalEmployees;
    private Long totalTeams;
    private Long totalProjects;
    private java.util.List<com.organization.taskManagement.Model.ProjectModel> recentProjects;
    private java.util.List<com.organization.taskManagement.DTO.Response.EmployeeRegistrationResponseDTO> recentEmployees;
    private java.util.List<com.organization.taskManagement.Model.TaskModel> upcomingDeadlines;
    private Map<String, Long> teamDistribution;
}

