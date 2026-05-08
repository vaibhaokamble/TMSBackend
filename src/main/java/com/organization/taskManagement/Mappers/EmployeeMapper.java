package com.organization.taskManagement.Mappers;

import com.organization.taskManagement.DTO.Request.EmployeeRegistrationRequestDTO;
import com.organization.taskManagement.DTO.Response.EmployeeRegistrationResponseDTO;
import com.organization.taskManagement.Model.EmployeeRegisterModel;

public class EmployeeMapper {

    public static EmployeeRegisterModel toEntity(EmployeeRegistrationRequestDTO request){

        if(request == null) return null;

        return EmployeeRegisterModel.builder()
                .name(request.getName())
                .email(request.getEmail())
                .employeeId(request.getEmployeeId())
                .password(request.getPassword())
                .role(request.getRole())
                .designation(request.getDesignation())
                .build();
    }

    public static EmployeeRegistrationResponseDTO toResponse(EmployeeRegisterModel employee){

        if(employee == null) return  null;

        return EmployeeRegistrationResponseDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .employeeId(employee.getEmployeeId())
                .role(employee.getRole())
                .designation(employee.getDesignation())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }

}
