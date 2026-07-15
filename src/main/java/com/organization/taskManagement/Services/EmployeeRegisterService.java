package com.organization.taskManagement.Services;

import com.organization.taskManagement.DTO.Request.EmployeeUpdateRequestDTO;
import com.organization.taskManagement.DTO.Response.EmployeeRegistrationResponseDTO;
import com.organization.taskManagement.Mappers.EmployeeMapper;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Repository.EmployeeRegisterRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class EmployeeRegisterService {

    private final EmployeeRegisterRepository employeeRegRepo;
    private final PasswordEncoder passwordEncoder;
    private final ActivityLogService activityLogService;

    //delete employee by id
    public void deleteEmployee (Long id){
        EmployeeRegisterModel employee = employeeRegRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        String name = employee.getName();
        employeeRegRepo.delete(employee);
        activityLogService.logActivity("Employee Deleted", "Employee deleted: " + name);
    }

    public Page<EmployeeRegistrationResponseDTO> getAllEmployees(Pageable pageable, com.organization.taskManagement.security.UserInfoDetails userDetails) {
        EmployeeRegisterModel currentEmployee = employeeRegRepo.findByEmployeeId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        if (currentEmployee.getRole() == com.organization.taskManagement.Enums.EmployeeRole.EMPLOYEE) {
            // If EMPLOYEE, return only their own profile
            return new org.springframework.data.domain.PageImpl<>(
                    java.util.Collections.singletonList(EmployeeMapper.toResponse(currentEmployee)), 
                    pageable, 
                    1
            );
        }
        
        // ADMIN or TEAM_LEAD can see all employees
        return employeeRegRepo.findAll(pageable)
                .map(EmployeeMapper::toResponse);
    }


    public EmployeeRegistrationResponseDTO updateEmployee(@Valid @PathVariable String employeeId, @RequestBody EmployeeUpdateRequestDTO employeeRequest) {
        EmployeeRegisterModel employeeRegisterModel = employeeRegRepo.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

        if (employeeRequest.getName() != null) {
            employeeRegisterModel.setName(employeeRequest.getName());
        }

        if (employeeRequest.getEmail() != null) {
            employeeRegisterModel.setEmail(employeeRequest.getEmail());
        }

        if (employeeRequest.getDesignation() != null) {
            employeeRegisterModel.setDesignation(employeeRequest.getDesignation());
        }

        if (employeeRequest.getRole() != null) {
            employeeRegisterModel.setRole(employeeRequest.getRole());
        }

        employeeRegRepo.save(employeeRegisterModel);
        
        activityLogService.logActivity("Employee Updated", "Employee profile updated for: " + employeeRegisterModel.getName());

        return EmployeeMapper.toResponse(employeeRegisterModel);
    }

    public EmployeeRegistrationResponseDTO updateProfile(Long id, String name, String profilePictureUrl) {
        EmployeeRegisterModel employee = employeeRegRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        if (name != null) employee.setName(name);
        if (profilePictureUrl != null) employee.setProfilePictureUrl(profilePictureUrl);
        
        employeeRegRepo.save(employee);
        
        activityLogService.logActivity("Profile Updated", "Profile picture updated for: " + employee.getName());
        
        return EmployeeMapper.toResponse(employee);
    }

    public void changePassword(Long id, String oldPassword, String newPassword) {
        EmployeeRegisterModel employee = employeeRegRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
                
        if (!passwordEncoder.matches(oldPassword, employee.getPassword())) {
            throw new RuntimeException("Old password does not match");
        }
        
        employee.setPassword(passwordEncoder.encode(newPassword));
        employeeRegRepo.save(employee);
        
        activityLogService.logActivity("Password Changed", "Password changed for: " + employee.getName());
    }
}
