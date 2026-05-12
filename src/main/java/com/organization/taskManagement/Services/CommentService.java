package com.organization.taskManagement.Services;

import com.organization.taskManagement.DTO.Request.CommentRequestDTO;
import com.organization.taskManagement.DTO.Response.CommentResponseDTO;
import com.organization.taskManagement.Mappers.CommentMapper;
import com.organization.taskManagement.Model.CommentModel;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Model.TaskModel;
import com.organization.taskManagement.Repository.CommentRepository;
import com.organization.taskManagement.Repository.EmployeeRegisterRepository;
import com.organization.taskManagement.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final EmployeeRegisterRepository employeeRegisterRepository;

    //comment service
    public CommentResponseDTO addComment(Long taskId, CommentRequestDTO requestDTO) {
        TaskModel task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        EmployeeRegisterModel employee = employeeRegisterRepository.findByEmployeeId(requestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + requestDTO.getEmployeeId()));

        CommentModel comment = CommentMapper.toEntity(requestDTO, task, employee);
        CommentModel savedComment = commentRepository.save(comment);
        return CommentMapper.toResponse(savedComment);
         }
}
