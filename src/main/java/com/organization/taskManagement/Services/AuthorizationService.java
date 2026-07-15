package com.organization.taskManagement.Services;

import com.organization.taskManagement.DTO.Request.EmployeeRegistrationRequestDTO;
import com.organization.taskManagement.DTO.Request.LoginRequestDTO;
import com.organization.taskManagement.DTO.Request.OtpRequestDTO;
import com.organization.taskManagement.DTO.Request.ResetPasswordRequestDTO;
import com.organization.taskManagement.DTO.Request.VerifyOtpRequestDTO;
import com.organization.taskManagement.DTO.Response.EmployeeRegistrationResponseDTO;
import com.organization.taskManagement.DTO.Response.LoginResponseDTO;
import com.organization.taskManagement.Mappers.EmployeeMapper;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Model.OtpVerificationModel;
import com.organization.taskManagement.Repository.EmployeeRegisterRepository;
import com.organization.taskManagement.Repository.OtpVerificationRepository;
import com.organization.taskManagement.security.JwtService;
import com.organization.taskManagement.security.UserInfoDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final EmployeeRegisterRepository employeeRegisterRepository;
    private final OtpVerificationRepository otpVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final ActivityLogService activityLogService;
    
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_TIME_DURATION = 15; // minutes

    public String sendRegisterOtp(String email) {
        if (employeeRegisterRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        String otp = String.format("%06d", new Random().nextInt(999999));
        OtpVerificationModel otpModel = otpVerificationRepository.findByEmail(email).orElse(new OtpVerificationModel());
        otpModel.setEmail(email);
        otpModel.setOtp(otp);
        otpModel.setExpiry(LocalDateTime.now().plusMinutes(5));
        otpModel.setVerified(false);
        
        otpVerificationRepository.save(otpModel);
        emailService.sendOtpEmail(email, "Future User", otp, "Email Verification");
        
        return "OTP sent successfully to your email.";
    }

    public String verifyRegisterOtp(String email, String otp) {
        OtpVerificationModel otpModel = otpVerificationRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No pending verification found for this email"));

        if (otpModel.isVerified()) {
            return "Email is already verified";
        }

        if (otpModel.getExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        if (!otpModel.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        otpModel.setVerified(true);
        otpVerificationRepository.save(otpModel);
        return "Email verified successfully";
    }

    public EmployeeRegistrationResponseDTO registerEmployee(@Valid EmployeeRegistrationRequestDTO request) {
        if(employeeRegisterRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new RuntimeException("Employee ID already exists");
        }

        if (employeeRegisterRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        OtpVerificationModel otpModel = otpVerificationRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Please verify your email before registering"));

        if (!otpModel.isVerified()) {
            throw new RuntimeException("Please verify your email before registering");
        }

        EmployeeRegisterModel employee = EmployeeMapper.toEntity(request);
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setEmailVerified(true);
        
        EmployeeRegisterModel result = employeeRegisterRepository.save(employee);
        
        // Cleanup the temporary OTP record
        otpVerificationRepository.delete(otpModel);
        
        activityLogService.logActivity(result, "Employee Registered", "Employee registered: " + result.getName());
        
        return EmployeeMapper.toResponse(result);
    }
    
    public String verifyEmail(VerifyOtpRequestDTO request) {
        EmployeeRegisterModel employee = employeeRegisterRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));
                
        if (employee.isEmailVerified()) {
            throw new RuntimeException("Email is already verified");
        }

        if (employee.getEmailVerificationOtp() == null || !employee.getEmailVerificationOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (employee.getEmailVerificationOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        employee.setEmailVerified(true);
        employee.setEmailVerificationOtp(null);
        employee.setEmailVerificationOtpExpiry(null);
        employeeRegisterRepository.save(employee);
        
        activityLogService.logActivity(employee, "Email Verified", "Email verified for: " + employee.getName());
        
        return "Email verified successfully";
    }
    
    public String resendOtp(OtpRequestDTO request) {
        EmployeeRegisterModel employee = employeeRegisterRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));
                
        if (employee.isEmailVerified()) {
            throw new RuntimeException("Email is already verified");
        }
        
        if (employee.getEmailVerificationOtpExpiry() != null) {
            long secondsSinceLastOtp = ChronoUnit.SECONDS.between(employee.getEmailVerificationOtpExpiry().minusMinutes(5), LocalDateTime.now());
            if (secondsSinceLastOtp < 60) {
                throw new RuntimeException("Please wait 60 seconds before requesting a new OTP");
            }
        }
        
        String otp = String.format("%06d", new Random().nextInt(999999));
        employee.setEmailVerificationOtp(otp);
        employee.setEmailVerificationOtpExpiry(LocalDateTime.now().plusMinutes(5));
        employeeRegisterRepository.save(employee);
        
        emailService.sendOtpEmail(employee.getEmail(), employee.getName(), otp, "Email Verification");
        
        return "OTP resent successfully";
    }

    public LoginResponseDTO login(@Valid LoginRequestDTO request) {
        EmployeeRegisterModel employee = employeeRegisterRepository.findByEmployeeId(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!employee.isEmailVerified()) {
            throw new RuntimeException("Please verify your email before logging in.");
        }
        
        if (employee.isAccountLocked()) {
            if (employee.getAccountLockedUntil() != null && employee.getAccountLockedUntil().isAfter(LocalDateTime.now())) {
                throw new RuntimeException("Account is locked due to too many failed attempts. Try again later.");
            } else {
                employee.setAccountLocked(false);
                employee.setAccountLockedUntil(null);
                employee.setFailedLoginAttempts(0);
                employeeRegisterRepository.save(employee);
            }
        }

        if (employee.getRole() != request.getRole()) {
            recordFailedAttempt(employee);
            throw new RuntimeException("Invalid role");
        }

        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            recordFailedAttempt(employee);
            throw new RuntimeException("Invalid password");
        }
        
        employee.setFailedLoginAttempts(0);
        employee.setAccountLocked(false);
        employee.setAccountLockedUntil(null);
        employee.setLastLogin(LocalDateTime.now());
        employeeRegisterRepository.save(employee);

        String token = jwtService.generateToken(employee.getEmployeeId(), new UserInfoDetails(employee));

        activityLogService.logActivity(employee, "Employee Login", "Employee logged in: " + employee.getName());

        return LoginResponseDTO.builder()
                .token(token)
                .employeeId(employee.getEmployeeId())
                .role(employee.getRole())
                .name(employee.getName())
                .build();
    }
    
    private void recordFailedAttempt(EmployeeRegisterModel employee) {
        int attempts = employee.getFailedLoginAttempts() + 1;
        employee.setFailedLoginAttempts(attempts);
        if (attempts >= MAX_FAILED_ATTEMPTS) {
            employee.setAccountLocked(true);
            employee.setAccountLockedUntil(LocalDateTime.now().plusMinutes(LOCK_TIME_DURATION));
        }
        employeeRegisterRepository.save(employee);
    }

    public String forgotPassword(OtpRequestDTO request) {
        EmployeeRegisterModel employee = employeeRegisterRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        String otp = String.format("%06d", new Random().nextInt(999999));
        employee.setResetPasswordOtp(otp);
        employee.setResetPasswordOtpExpiry(LocalDateTime.now().plusMinutes(5));
        employeeRegisterRepository.save(employee);

        emailService.sendOtpEmail(employee.getEmail(), employee.getName(), otp, "Password Reset");
        return "Password reset OTP sent successfully";
    }

    public String verifyResetOtp(VerifyOtpRequestDTO request) {
        EmployeeRegisterModel employee = employeeRegisterRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (employee.getResetPasswordOtp() == null || !employee.getResetPasswordOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (employee.getResetPasswordOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        return "OTP verified successfully";
    }

    public String resetPassword(ResetPasswordRequestDTO request) {
        EmployeeRegisterModel employee = employeeRegisterRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (employee.getResetPasswordOtp() == null || !employee.getResetPasswordOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (employee.getResetPasswordOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        if (passwordEncoder.matches(request.getNewPassword(), employee.getPassword())) {
            throw new RuntimeException("New password cannot be the same as the old password. Please choose another one.");
        }

        employee.setPassword(passwordEncoder.encode(request.getNewPassword()));
        employee.setPasswordChangedAt(LocalDateTime.now());
        employee.setResetPasswordOtp(null);
        employee.setResetPasswordOtpExpiry(null);
        employee.setFailedLoginAttempts(0); // Unlock if locked
        employee.setAccountLocked(false);
        employee.setAccountLockedUntil(null);
        employeeRegisterRepository.save(employee);

        activityLogService.logActivity(employee, "Password Changed", "Password reset for: " + employee.getName());

        return "Password reset successfully";
    }
}
