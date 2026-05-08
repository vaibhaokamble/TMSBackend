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

    //delete employee by id
    public void deleteEmployee (Long id){
        EmployeeRegisterModel employee = employeeRegRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        employeeRegRepo.delete(employee);
    }

    //get all employee
    public Page<EmployeeRegistrationResponseDTO> getAllEmployees(Pageable pageable) {
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

        return EmployeeMapper.toResponse(employeeRegisterModel);
    }
}
