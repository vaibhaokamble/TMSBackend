package com.organization.taskManagement.Mappers;

import com.organization.taskManagement.DTO.Request.CommentRequestDTO;
import com.organization.taskManagement.DTO.Response.CommentResponseDTO;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Model.CommentModel;
import com.organization.taskManagement.Model.TaskModel;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CommentMapper {

    public static CommentModel toEntity(CommentRequestDTO request, TaskModel task, EmployeeRegisterModel employee) {
        if (request == null) return null;

        return CommentModel.builder()
                .message(request.getMessage()) // Check if DTO has getText() or getMessage()
                .employeeId(employee)         // Changed from employeeId to employee
                .task(task)
                .name(employee != null ? employee.getName(): request.getName())
                .build();
    }

    public static CommentResponseDTO toResponse(CommentModel comment) {
        if (comment == null) return null;


        CommentResponseDTO response = new CommentResponseDTO();
        response.setId(comment.getId()!= null? String.valueOf(comment.getId()): null);
        response.setEmployeeId(comment.getEmployeeId()!= null ? comment.getEmployeeId().getEmployeeId() : null);
        response.setName(comment.getEmployeeId()!= null ? comment.getEmployeeId().getName() : null);
        response.setMessage(comment.getMessage());



        if (comment.getCreatedAt() != null) {
            response.setTimestamp(
                    comment.getCreatedAt()
                            .atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            );
        }

        return response;
    }
}
