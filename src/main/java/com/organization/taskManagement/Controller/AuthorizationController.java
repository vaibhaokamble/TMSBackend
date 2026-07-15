package com.organization.taskManagement.Controller;

import com.organization.taskManagement.DTO.Request.EmployeeRegistrationRequestDTO;
import com.organization.taskManagement.DTO.Request.LoginRequestDTO;
import com.organization.taskManagement.DTO.Request.OtpRequestDTO;
import com.organization.taskManagement.DTO.Request.ResetPasswordRequestDTO;
import com.organization.taskManagement.DTO.Request.VerifyOtpRequestDTO;
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
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping("/register/send-otp")
    public ResponseEntity<ApiResponseDTO<String>> sendRegisterOtp(@Valid @RequestBody OtpRequestDTO request) {
        String response = authorizationService.sendRegisterOtp(request.getEmail());
        return ResponseEntity.ok(ApiResponseDTO.success("Success", response));
    }

    @PostMapping("/register/verify-otp")
    public ResponseEntity<ApiResponseDTO<String>> verifyRegisterOtp(@Valid @RequestBody VerifyOtpRequestDTO request) {
        String response = authorizationService.verifyRegisterOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(ApiResponseDTO.success("Success", response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<EmployeeRegistrationResponseDTO>> register(@Valid @RequestBody EmployeeRegistrationRequestDTO request) {
        EmployeeRegistrationResponseDTO response = authorizationService.registerEmployee(request);
        return ResponseEntity.ok(ApiResponseDTO.success("Registration successful. Please verify your email.", response));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponseDTO<String>> verifyEmail(@Valid @RequestBody VerifyOtpRequestDTO request) {
        String response = authorizationService.verifyEmail(request);
        return ResponseEntity.ok(ApiResponseDTO.success("Success", response));
    }
    
    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponseDTO<String>> resendOtp(@Valid @RequestBody OtpRequestDTO request) {
        String response = authorizationService.resendOtp(request);
        return ResponseEntity.ok(ApiResponseDTO.success("Success", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authorizationService.login(request);
        return ResponseEntity.ok(ApiResponseDTO.success("Login successful", response));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponseDTO<String>> forgotPassword(@Valid @RequestBody OtpRequestDTO request) {
        String response = authorizationService.forgotPassword(request);
        return ResponseEntity.ok(ApiResponseDTO.success("Success", response));
    }

    @PostMapping("/verify-reset-otp")
    public ResponseEntity<ApiResponseDTO<String>> verifyResetOtp(@Valid @RequestBody VerifyOtpRequestDTO request) {
        String response = authorizationService.verifyResetOtp(request);
        return ResponseEntity.ok(ApiResponseDTO.success("Success", response));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDTO<String>> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        String response = authorizationService.resetPassword(request);
        return ResponseEntity.ok(ApiResponseDTO.success("Success", response));
    }
}
