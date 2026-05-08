package com.organization.taskManagement.Services;

import com.organization.taskManagement.DTO.Response.AnalyticsResponseDTO;
import com.organization.taskManagement.Enums.TaskStatus;
import com.organization.taskManagement.Model.TaskModel;
import com.organization.taskManagement.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TaskRepository taskRepo;

    public AnalyticsResponseDTO getAnalyticsOverview() {
        // Get total task count
        long totalTasks = taskRepo.count();

        // Get completed tasks count
        long completedTasks = taskRepo.countByStatus(TaskStatus.DONE);

        // Get pending tasks count (NEW + ASSIGN)
        long newTasks = taskRepo.countByStatus(TaskStatus.NEW);
        long assignedTasks = taskRepo.countByStatus(TaskStatus.ASSIGN);
        long pendingTasks = newTasks + assignedTasks;

        // Get team distribution by designation
        Map<String, Long> teamDistribution = getTeamDistribution();

        return AnalyticsResponseDTO.builder()
                .totalTasks(totalTasks)
                .completedTasks(completedTasks)
                .pendingTasks(pendingTasks)
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

