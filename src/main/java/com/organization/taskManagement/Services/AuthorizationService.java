package com.organization.taskManagement.Services;

import com.organization.taskManagement.DTO.Request.EmployeeRegistrationRequestDTO;
import com.organization.taskManagement.DTO.Request.LoginRequestDTO;
import com.organization.taskManagement.DTO.Response.EmployeeRegistrationResponseDTO;
import com.organization.taskManagement.DTO.Response.LoginResponseDTO;
import com.organization.taskManagement.Mappers.EmployeeMapper;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Repository.EmployeeRegisterRepository;
import com.organization.taskManagement.security.JwtService;
import com.organization.taskManagement.security.UserInfoDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final EmployeeRegisterRepository employeeRegisterRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public EmployeeRegistrationResponseDTO registerEmployee(@Valid EmployeeRegistrationRequestDTO request) {
        if(employeeRegisterRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new RuntimeException("Employee ID already exists");
        }

        if (employeeRegisterRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        EmployeeRegisterModel employeeRegisterModel = EmployeeMapper.toEntity(request);
        employeeRegisterModel.setPassword(passwordEncoder.encode(request.getPassword()));
        EmployeeRegisterModel result = employeeRegisterRepository.save(employeeRegisterModel);

        return EmployeeMapper.toResponse(result);
    }

    public LoginResponseDTO login(@Valid LoginRequestDTO request) {
        EmployeeRegisterModel employeeRegisterModel = employeeRegisterRepository.findByEmployeeId(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (employeeRegisterModel.getRole() != request.getRole()) {
            throw new RuntimeException("Invalid role");
        }

        if (!passwordEncoder.matches(request.getPassword(), employeeRegisterModel.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(employeeRegisterModel.getEmployeeId(), new UserInfoDetails(employeeRegisterModel));

        return LoginResponseDTO.builder()
                .token(token)
                .employeeId(employeeRegisterModel.getEmployeeId())
                .role(employeeRegisterModel.getRole())
                .name(employeeRegisterModel.getName())
                .build();
    }
}
