package com.organization.taskManagement.Exception;

import com.organization.taskManagement.DTO.Response.ApiResponseDTO;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handles validation errors from @Valid in Controller
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        log.warn("Validation failed: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.failure("Validation failed. Please check the errors.", errors));
    }

    // 2. Handles validation errors from @Validated in Service/Entity (JPA validation)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> handleConstraintViolation(
            ConstraintViolationException ex) {

        log.warn("Constraint violation: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            // Clean the path (e.g., "registerEmployee.request.email" -> "email")
            if (fieldName.contains(".")) {
                fieldName = fieldName.substring(fieldName.lastIndexOf('.') + 1);
            }
            errors.put(fieldName, violation.getMessage());
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.failure("Validation failed. Please check the errors.", errors));
    }

    // 3. Handles duplicate data (unique constraint violations)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {

        log.error("Data integrity violation: {}", ex.getMessage());

        String message = "Database constraint violation. ";
        if (ex.getMessage().contains("Duplicate entry")) {
            if (ex.getMessage().contains("email")) {
                message = "Email already exists. Please use a different email address.";
            } else if (ex.getMessage().contains("employee_id")) {
                message = "Employee ID already exists. Please use a different Employee ID.";
            } else {
                message = "Duplicate entry detected. The value already exists in the system.";
            }
        } else if (ex.getMessage().contains("cannot be null")) {
            message = "Required field cannot be null. Please provide all required fields.";
        } else {
            message = "Database constraint violation occurred.";
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponseDTO.failure(message, null));
    }

    // 4. Handles generic database errors
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleDataAccessException(
            DataAccessException ex) {

        log.error("Database error: {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.failure("Database operation failed. Please try again later.", null));
    }

    // 5. Handles invalid JSON format in request body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {

        log.warn("Invalid request body format: {}", ex.getMessage());

        String message = "Invalid request format. ";
        if (ex.getMessage().contains("Enum")) {
            message += "Please check enum values (role, designation).";
        } else if (ex.getMessage().contains("JSON")) {
            message += "Please check your JSON syntax.";
        } else {
            message += "Please check your request body format.";
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.failure(message, null));
    }

    // 6. Handles missing required request parameters
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleMissingParams(
            MissingServletRequestParameterException ex) {

        log.warn("Missing parameter: {}", ex.getParameterName());

        String message = String.format("Required parameter '%s' is missing.", ex.getParameterName());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.failure(message, null));
    }

    // 7. Handles type mismatch in path variables or request parameters
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        log.warn("Type mismatch: {}", ex.getMessage());

        String message = String.format(
                "Invalid value '%s' for parameter '%s'. Expected type: %s.",
                ex.getValue(),
                ex.getName(),
                ex.getRequiredType().getSimpleName()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.failure(message, null));
    }

    // 8. Handles custom business exceptions (you can add your own)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleIllegalArgument(
            IllegalArgumentException ex) {

        log.warn("Illegal argument: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.failure(ex.getMessage(), null));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleIllegalState(
            IllegalStateException ex) {

        log.warn("Illegal state: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponseDTO.failure(ex.getMessage(), null));
    }

    // 9. Handles generic runtime exceptions (catch-all)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleRuntimeException(
            RuntimeException ex) {

        log.error("Runtime exception: {}", ex.getMessage(), ex);

        // Don't expose internal errors to client in production
        String message = "An unexpected error occurred. Please try again later.";

        // For development, you might want to see the actual error
        if (isDevEnvironment()) {
            message = ex.getMessage();
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.failure(message, null));
    }

    // 10. Handles all other exceptions (catch-all)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleGenericException(
            Exception ex) {

        log.error("Unexpected error: {}", ex.getMessage(), ex);

        String message = "System error occurred. Please contact support.";

        if (isDevEnvironment()) {
            message = ex.getMessage();
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.failure(message, null));
    }

    // Helper method to check if in development environment
    private boolean isDevEnvironment() {
        // You can inject @Value("${spring.profiles.active}") and check
        return true; // For demo, always return true
        // In production, return false
    }
}