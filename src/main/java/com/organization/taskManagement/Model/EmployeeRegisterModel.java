package com.organization.taskManagement.Model;

import com.organization.taskManagement.Enums.EmployeeRole;
import com.organization.taskManagement.Enums.Designation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
@Builder
public class EmployeeRegisterModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String employeeId;

    @Column(nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeRole role;

    @Enumerated(EnumType.STRING)
    private Designation designation;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    // Email Verification Fields
    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    @Column(name = "email_verification_otp")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String emailVerificationOtp;

    @Column(name = "email_verification_otp_expiry")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private LocalDateTime emailVerificationOtpExpiry;

    // Reset Password Fields
    @Column(name = "reset_password_otp")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String resetPasswordOtp;

    @Column(name = "reset_password_otp_expiry")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private LocalDateTime resetPasswordOtpExpiry;

    // Security Fields
    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts = 0;

    @Column(name = "account_locked", nullable = false)
    private boolean accountLocked = false;

    @Column(name = "account_locked_until")
    private LocalDateTime accountLockedUntil;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;

    @ManyToMany(mappedBy = "members")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.List<TeamModel> teams;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
