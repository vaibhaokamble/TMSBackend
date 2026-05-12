package com.organization.taskManagement.Mappers;

import com.organization.taskManagement.DTO.Request.CommentRequestDTO;
import com.organization.taskManagement.DTO.Response.CommentResponseDTO;
import com.organization.taskManagement.Model.CommentModel;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Model.TaskModel;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CommentMapper {

    public static CommentModel toEntity(CommentRequestDTO request, TaskModel task, EmployeeRegisterModel employee) {

        if (request == null) return null;

        return CommentModel.builder()
                .message(request.getMessage())
                .employeeId(employee)
                .task(task)
                .build();
    }

    public  static CommentResponseDTO toResponse(CommentModel commentModel){
        if (commentModel == null) return null;
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setId(commentModel.getId()!= null ? String .valueOf(commentModel.getId()) : null);
        commentResponseDTO.setEmployeeId(commentModel.getEmployeeId() != null ? commentModel.getEmployeeId().getEmployeeId() : null);
        commentResponseDTO.setName(commentModel.getEmployeeId() != null ? commentModel.getEmployeeId().getName() : null);
        commentResponseDTO.setMessage(commentModel.getMessage());
        commentResponseDTO.setTimestamp(
                commentModel.getCreatedAt() == null
                        ? null
                        : commentModel.getCreatedAt()
                          .atZone(ZoneId.systemDefault())
                          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );

        return commentResponseDTO;
    }
}
