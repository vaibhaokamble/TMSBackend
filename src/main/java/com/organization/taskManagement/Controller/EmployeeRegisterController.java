package com.organization.taskManagement.Controller;


import com.organization.taskManagement.DTO.Response.ApiResponseDTO;
import com.organization.taskManagement.DTO.Request.EmployeeRegistrationRequestDTO;
import com.organization.taskManagement.DTO.Response.EmployeeRegistrationResponseDTO;
import com.organization.taskManagement.DTO.Request.LoginRequestDTO;
import com.organization.taskManagement.Services.EmployeeRegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class EmployeeRegisterController {

    private final EmployeeRegisterService employeeRegisterService;

    @GetMapping
    public ResponseEntity<Page<EmployeeRegistrationResponseDTO>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC , sortBy);

        return ResponseEntity.ok(employeeRegisterService.getAllEmployees(pageable));
    }

    @DeleteMapping("/{id}")
   public ResponseEntity<ApiResponseDTO<?>> deleteEmployee(@PathVariable Long id){
        employeeRegisterService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Employee deleted successfully", null));
   }

}
