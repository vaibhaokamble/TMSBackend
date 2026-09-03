package com.organization.taskManagement.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API response wrapper to maintain uniform JSON structure across all endpoints.
 * @param <T> The type of the payload data.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    
    /**
     * Creates a successful response.
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }
    
    /**
     * Creates a successful response without data.
     */
    public static <T> ApiResponse<T> success(String message) {
        return success(message, null);
    }
    
    /**
     * Creates an error response.
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}
