package com.organization.taskManagement.Controller;

import com.organization.taskManagement.DTO.Request.EmployeeRegistrationRequestDTO;
import com.organization.taskManagement.DTO.Request.LoginRequestDTO;
import com.organization.taskManagement.DTO.Response.ApiResponseDTO;
import com.organization.taskManagement.DTO.Response.EmployeeRegistrationResponseDTO;
import com.organization.taskManagement.DTO.Response.LoginResponseDTO;
import com.organization.taskManagement.Services.AuthorizationService;
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

    private final AuthorizationService authorizationService;

    //TODO register employee post mapping
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<EmployeeRegistrationResponseDTO>> register(@Valid @RequestBody EmployeeRegistrationRequestDTO request) {
        EmployeeRegistrationResponseDTO response = authorizationService.registerEmployee(request);
        return ResponseEntity.ok(ApiResponseDTO.success("Employee registered successfully", response));
    }

    //TODO login employee post mapping
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authorizationService.login(request);
        return ResponseEntity.ok(ApiResponseDTO.success("Login successful", response));
    }

}
