package com.organization.taskManagement.Services;

import com.organization.taskManagement.DTO.Request.EmployeeRegistrationRequestDTO;
import com.organization.taskManagement.DTO.Response.EmployeeRegistrationResponseDTO;
import com.organization.taskManagement.DTO.Request.LoginRequestDTO;
import com.organization.taskManagement.Mappers.EmployeeMapper;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Repository.EmployeeRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeRegisterService {

    private final EmployeeRegisterRepository employeeRegRepo;
    private final PasswordEncoder passwordEncoder;

    public void deleteEmployee (Long id){
        EmployeeRegisterModel employee = employeeRegRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        employeeRegRepo.delete(employee);
    }

    public Page<EmployeeRegistrationResponseDTO> getAllEmployees(Pageable pageable) {
        return employeeRegRepo.findAll(pageable)
                .map(EmployeeMapper::toResponse);
    }


}
