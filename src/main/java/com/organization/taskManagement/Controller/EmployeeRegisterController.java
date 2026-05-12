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

@RestController
@RequiredArgsConstructor
public class EmployeeRegisterController {

    private final EmployeeRegisterService employeeRegisterService;


    //TODO get mapping by pagination and sorting
    @GetMapping("/employees")
    public ResponseEntity<Page<EmployeeRegistrationResponseDTO>> getAllEmployees(@Valid
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC , sortBy);

        return ResponseEntity.ok(employeeRegisterService.getAllEmployees(pageable));
    }

    //TODO patch mapping by name, email, designation, role
    @PatchMapping("update/{employeeId}")
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

}
