package com.organization.taskManagement.Repository;

import com.organization.taskManagement.Model.RefreshTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenModel, Long> {
    Optional<RefreshTokenModel> findByToken(String token);
    void deleteByEmployeeId(String employeeId);
}
