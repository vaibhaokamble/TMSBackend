package com.organization.taskManagement.Repository;

import com.organization.taskManagement.Model.EmployeeRegisterModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.ScopedValue;
import java.util.Optional;

public interface EmployeeRegisterRepository extends JpaRepository<EmployeeRegisterModel, Long> {




    boolean existsByEmail(String email);

    boolean existsByEmployeeId(String employeeId);

    Optional<EmployeeRegisterModel> findByEmail(String email);

    Optional<EmployeeRegisterModel> findByEmployeeId(String employeeId);

}
