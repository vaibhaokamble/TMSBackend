package com.organization.taskManagement.security;

import com.organization.taskManagement.DTO.Request.EmployeeRegistrationRequestDTO;
import com.organization.taskManagement.Mappers.EmployeeMapper;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Repository.EmployeeRegisterRepository;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * STEP-BY-STEP INTERNAL WORKINGS:
 * 
 * 1. This service implements UserDetailsService, which is how Spring Security retrieves user data.
 * 2. It connects Spring Security to our database (EmployeeRegisterRepository).
 * 3. It also handles adding new employees with encrypted passwords.
 */
@Service
public class UserInfoService implements UserDetailsService {

    private final EmployeeRegisterRepository employeeRegRepo;
    private final PasswordEncoder passwordEncoder;

    public UserInfoService(EmployeeRegisterRepository employeeRegRepo, PasswordEncoder passwordEncoder) {
        this.employeeRegRepo = employeeRegRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * INTERNAL FLOW: Loading User
     * 1. Spring Security calls this when it needs to verify a user (e.g., during login or JWT validation).
     * 2. We search the database by employeeId.
     * 3. If found, we wrap the employee entity in a UserInfoDetails object.
     * 4. If not found, we throw a UsernameNotFoundException.
     */
    @Override
    public UserDetails loadUserByUsername(@NonNull String employeeId) throws UsernameNotFoundException {
        Optional<EmployeeRegisterModel> employeeOpt = employeeRegRepo.findByEmployeeId(employeeId);
        if(employeeOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found with employee ID: " + employeeId);
        }

        EmployeeRegisterModel employee = employeeOpt.get();
        return new UserInfoDetails(employee);
    }

    /**
     * INTERNAL FLOW: Registration
     * 1. Checks if the employee ID already exists.
     * 2. Encrypts the raw password using BCrypt.
     * 3. Maps the DTO to an Entity and saves it.
     */
    public String addEmployee(EmployeeRegistrationRequestDTO employeeDto) {
        if(employeeRegRepo.findByEmployeeId(employeeDto.getEmployeeId()).isPresent()) {
            return "Employee ID already exists";
        }
        
        // Encrypt the password before saving
        employeeDto.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
        
        employeeRegRepo.save(EmployeeMapper.toEntity(employeeDto));
        return "Employee added successfully";
    }
}
