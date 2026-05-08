package com.organization.taskManagement.Controller;

import com.organization.taskManagement.DTO.Response.AnalyticsResponseDTO;
import com.organization.taskManagement.DTO.Response.ApiResponseDTO;
import com.organization.taskManagement.Services.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/overview")
    public ResponseEntity<ApiResponseDTO<AnalyticsResponseDTO>> getAnalyticsOverview() {
            AnalyticsResponseDTO analyticsResponse = analyticsService.getAnalyticsOverview();
            return ResponseEntity.ok(ApiResponseDTO.success("Analytics overview retrieved successfully", analyticsResponse));
    }
}

