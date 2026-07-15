package com.organization.taskManagement.Services;

import com.organization.taskManagement.DTO.Response.AnalyticsResponseDTO;
import com.organization.taskManagement.DTO.Response.EmployeeRegistrationResponseDTO;
import com.organization.taskManagement.Enums.TaskStatus;
import com.organization.taskManagement.Model.TaskModel;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Model.ProjectModel;
import com.organization.taskManagement.Repository.TaskRepository;
import com.organization.taskManagement.Repository.EmployeeRegisterRepository;
import com.organization.taskManagement.Repository.TeamRepository;
import com.organization.taskManagement.Repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TaskRepository taskRepo;
    private final EmployeeRegisterRepository employeeRepo;
    private final TeamRepository teamRepo;
    private final ProjectRepository projectRepo;

    public AnalyticsResponseDTO getAnalyticsOverview() {
        long totalTasks = taskRepo.count();
        long completedTasks = taskRepo.countByStatus(TaskStatus.DONE);
        long newTasks = taskRepo.countByStatus(TaskStatus.NEW);
        long assignedTasks = taskRepo.countByStatus(TaskStatus.ASSIGNED);
        long inProgressTasks = taskRepo.countByStatus(TaskStatus.IN_PROGRESS);
        
        long totalEmployees = employeeRepo.count();
        long totalTeams = teamRepo.count();
        long totalProjects = projectRepo.count();

        List<TaskModel> allTasks = taskRepo.findAll();
        LocalDate today = LocalDate.now();

        long overdueTasks = allTasks.stream()
            .filter(t -> t.getDueDate() != null && t.getDueDate().isBefore(today) && t.getStatus() != TaskStatus.DONE)
            .count();

        long todayAssignedTasks = allTasks.stream()
            .filter(t -> t.getDueDate() != null && t.getDueDate().isEqual(today))
            .count();

        List<ProjectModel> recentProjects = projectRepo.findAll().stream()
            .sorted(Comparator.comparing(ProjectModel::getCreatedAt).reversed())
            .limit(5)
            .collect(Collectors.toList());

        List<EmployeeRegistrationResponseDTO> recentEmployees = employeeRepo.findAll().stream()
            .sorted(Comparator.comparing(EmployeeRegisterModel::getCreatedAt).reversed())
            .limit(5)
            .map(e -> EmployeeRegistrationResponseDTO.builder()
                    .id(e.getId())
                    .name(e.getName())
                    .email(e.getEmail())
                    .employeeId(e.getEmployeeId())
                    .role(e.getRole())
                    .designation(e.getDesignation())
                    .createdAt(e.getCreatedAt())
                    .updatedAt(e.getUpdatedAt())
                    .build())
            .collect(Collectors.toList());

        List<TaskModel> upcomingDeadlines = allTasks.stream()
            .filter(t -> t.getDueDate() != null && !t.getDueDate().isBefore(today) && t.getStatus() != TaskStatus.DONE)
            .sorted(Comparator.comparing(TaskModel::getDueDate))
            .limit(5)
            .collect(Collectors.toList());

        Map<String, Long> teamDistribution = getTeamDistribution();

        return AnalyticsResponseDTO.builder()
                .totalTasks(totalTasks)
                .completedTasks(completedTasks)
                .newTasks(newTasks)
                .assignedTasks(assignedTasks)
                .inProgressTasks(inProgressTasks)
                .overdueTasks(overdueTasks)
                .todayAssignedTasks(todayAssignedTasks)
                .totalEmployees(totalEmployees)
                .totalTeams(totalTeams)
                .totalProjects(totalProjects)
                .recentProjects(recentProjects)
                .recentEmployees(recentEmployees)
                .upcomingDeadlines(upcomingDeadlines)
                .teamDistribution(teamDistribution)
                .build();
    }

    private Map<String, Long> getTeamDistribution() {
        List<TaskModel> assignedTasks = taskRepo.findAllAssignedTasks();
        Map<String, Long> distribution = new HashMap<>();

        for (TaskModel task : assignedTasks) {
            if (task.getAssignedTo() != null && task.getAssignedTo().getDesignation() != null) {
                String designation = task.getAssignedTo().getDesignation().toString();
                distribution.put(designation, distribution.getOrDefault(designation, 0L) + 1);
            }
        }
        return distribution;
    }
}
