package com.organization.taskManagement.DTO.Response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO{
    private String id;
    private String employeeId;
    private  String name;
    private String message;
    private String timestamp;


}