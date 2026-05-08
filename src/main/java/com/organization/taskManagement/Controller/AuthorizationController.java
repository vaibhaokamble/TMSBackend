package com.organization.taskManagement.Controller;

import com.organization.taskManagement.DTO.Request.EmployeeRegistrationRequestDTO;
import com.organization.taskManagement.DTO.Request.LoginRequestDTO;
import com.organization.taskManagement.DTO.Response.ApiResponseDTO;
import com.organization.taskManagement.DTO.Response.EmployeeRegistrationResponseDTO;
import com.organization.taskManagement.Services.EmployeeRegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {

    private EmployeeRegisterService employeeRegService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<EmployeeRegistrationResponseDTO>> register(@Valid @RequestBody EmployeeRegistrationRequestDTO request) {
        try {
            EmployeeRegistrationResponseDTO response = employeeRegService.registerEmployee(request);
            return ResponseEntity.ok(ApiResponseDTO.success("Employee registered successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.failure(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<EmployeeRegistrationResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            EmployeeRegistrationResponseDTO response = employeeRegService.login(request);
            return ResponseEntity.ok(ApiResponseDTO.success("Login successful", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.failure(e.getMessage()));
        }
    }

}
