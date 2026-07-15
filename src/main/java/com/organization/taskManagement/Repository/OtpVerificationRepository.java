package com.organization.taskManagement.Repository;

import com.organization.taskManagement.Model.OtpVerificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerificationModel, Long> {
    Optional<OtpVerificationModel> findByEmail(String email);
}
