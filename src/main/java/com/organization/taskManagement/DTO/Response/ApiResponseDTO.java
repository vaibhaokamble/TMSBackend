package com.organization.taskManagement.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponseDTO<T> success(String message, T data) {

        return new ApiResponseDTO<>(true, message, data);
    }

    public static <T> ApiResponseDTO<T> failure(String message) {
        return new ApiResponseDTO<>(false, message, null);
    }

    public static <T> ApiResponseDTO<T> failure(String message, T errors)
    {
        return new ApiResponseDTO<>(false, message, errors);
    }
}
