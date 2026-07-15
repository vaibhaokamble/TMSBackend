package com.organization.taskManagement.Controller;


import com.organization.taskManagement.DTO.Request.EmployeeUpdateRequestDTO;
import com.organization.taskManagement.DTO.Response.ApiResponseDTO;
import com.organization.taskManagement.DTO.Response.EmployeeRegistrationResponseDTO;
import com.organization.taskManagement.Services.EmployeeRegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeRegisterController {

    private final EmployeeRegisterService employeeRegisterService;


    @GetMapping
    public ResponseEntity<Page<EmployeeRegistrationResponseDTO>> getAllEmployees(
            @Valid @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @org.springframework.security.core.annotation.AuthenticationPrincipal com.organization.taskManagement.security.UserInfoDetails userDetails
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC , sortBy);
        return ResponseEntity.ok(employeeRegisterService.getAllEmployees(pageable, userDetails));
    }

    //TODO patch mapping by name, email, designation, role
    @PatchMapping("/{employeeId}")
    public ResponseEntity<ApiResponseDTO<EmployeeRegistrationResponseDTO>> updateEmployee(@Valid @PathVariable String employeeId, @RequestBody EmployeeUpdateRequestDTO employeeUpdateRequestDTO) {
        EmployeeRegistrationResponseDTO updatedEmployee = employeeRegisterService.updateEmployee(employeeId, employeeUpdateRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Employee updated successfully", updatedEmployee));
    }

    //TODO delete mapping by id
    @DeleteMapping("/{id}")
   public ResponseEntity<ApiResponseDTO<?>> deleteEmployee(@Valid @PathVariable Long id){
        employeeRegisterService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Employee deleted successfully", null));
   }

    @PutMapping("/profile/update")
    public ResponseEntity<ApiResponseDTO<EmployeeRegistrationResponseDTO>> updateProfile(
            @RequestBody java.util.Map<String, String> request,
            @org.springframework.security.core.annotation.AuthenticationPrincipal com.organization.taskManagement.security.UserInfoDetails userDetails
    ) {
        String name = request.get("name");
        String profilePictureUrl = request.get("profilePictureUrl");
        EmployeeRegistrationResponseDTO updated = employeeRegisterService.updateProfile(userDetails.getEmployee().getId(), name, profilePictureUrl);
        return ResponseEntity.ok(ApiResponseDTO.success("Profile updated", updated));
    }

    @PutMapping("/profile/change-password")
    public ResponseEntity<ApiResponseDTO<?>> changePassword(
            @RequestBody java.util.Map<String, String> request,
            @org.springframework.security.core.annotation.AuthenticationPrincipal com.organization.taskManagement.security.UserInfoDetails userDetails
    ) {
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");
        employeeRegisterService.changePassword(userDetails.getEmployee().getId(), oldPassword, newPassword);
        return ResponseEntity.ok(ApiResponseDTO.success("Password changed successfully", null));
    }
}
