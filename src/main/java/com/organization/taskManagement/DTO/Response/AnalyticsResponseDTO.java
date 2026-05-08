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
    private Long pendingTasks;
    private Map<String, Long> teamDistribution;
}

