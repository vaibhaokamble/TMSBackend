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
@Service
public class UserInfoService implements UserDetailsService {

    private final EmployeeRegisterRepository employeeRegRepo;
    private final PasswordEncoder passwordEncoder;

    public UserInfoService(EmployeeRegisterRepository employeeRegRepo, PasswordEncoder passwordEncoder) {
        this.employeeRegRepo = employeeRegRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String employeeId) throws UsernameNotFoundException {
        Optional<EmployeeRegisterModel> employeeOpt = employeeRegRepo.findByEmployeeId(employeeId);
        if(employeeOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found with employee ID: " + employeeId);
        }

        EmployeeRegisterModel employee = employeeOpt.get();
        return new UserInfoDetails(employee);
        }


        public String addEmployee(EmployeeRegistrationRequestDTO employee) {
            if(!employeeRegRepo.findByEmployeeId(employee.getEmployeeId()).isEmpty()) {
                return "Email already exists";
            }
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
            employeeRegRepo.save(EmployeeMapper.toEntity(employee));
            return "Employee added successfully";
        }
}

