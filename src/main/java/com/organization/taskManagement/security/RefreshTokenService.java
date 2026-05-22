package com.organization.taskManagement.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.organization.taskManagement.Model.RefreshTokenModel;
import com.organization.taskManagement.Repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

/**
 * STEP-BY-STEP INTERNAL WORKINGS:
 * 
 * 1. This service manages Refresh Tokens, which are used to get new JWTs when they expire.
 * 2. Refresh tokens are stored in the database for persistence.
 * 3. They have a longer expiration time (e.g., 7 days) compared to JWTs.
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * INTERNAL FLOW: Create Refresh Token
     * 1. Delete any existing tokens for this employee (only one active session allowed).
     * 2. Generate a new random UUID as the token.
     * 3. Set the associated employee, role, and expiration date.
     * 4. Save to database and return.
     */
    public RefreshTokenModel createRefreshToken(String employeeId, String employeeRole) {
        // Cleanup old tokens
        refreshTokenRepository.deleteByEmployeeId(employeeId);
        
        // Build new token object
        RefreshTokenModel refreshToken = RefreshTokenModel.builder()
                .token(UUID.randomUUID().toString())
                .employeeId(employeeId)
                .userType(employeeRole)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS)) // Valid for 7 days
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * INTERNAL FLOW: Expiration Check
     * 1. Compare the current time with the token's expiry date.
     * 2. If expired, remove from database and throw error.
     * 3. If valid, return the token.
     */
    public RefreshTokenModel verifyExpiration(RefreshTokenModel token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new login request");
        }
        return token;
    }
}
