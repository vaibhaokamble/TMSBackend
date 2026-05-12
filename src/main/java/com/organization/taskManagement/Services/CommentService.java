package com.organization.taskManagement.Services;

import com.organization.taskManagement.DTO.Request.CommentRequestDTO;
import com.organization.taskManagement.DTO.Response.CommentResponseDTO;
import com.organization.taskManagement.Mappers.CommentMapper; // You need your mapper
import com.organization.taskManagement.Model.CommentModel;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Model.TaskModel;
import com.organization.taskManagement.Repository.CommentRepository;
import com.organization.taskManagement.Repository.EmployeeRegisterRepository;
import com.organization.taskManagement.Repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final EmployeeRegisterRepository employeeRegisterRepository;


    @Transactional
    public CommentResponseDTO createComment(Long taskId, CommentRequestDTO requestDTO) {
        // 1. You MUST find the Task and Employee first

        TaskModel task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Assuming your EmployeeRegisterModel ID is a String like "EMP1"
        EmployeeRegisterModel employee = employeeRegisterRepository.findByEmployeeId(requestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + requestDTO.getEmployeeId()));
        // 1. Create the model (Consider using your CommentMapper here)
        CommentModel comment = CommentMapper.toEntity(requestDTO, task, employee);
        CommentModel savedComment = (CommentModel) commentRepository.save(comment);
        return CommentMapper.toResponse(savedComment);
    }


    // Remove or implement this to avoid "Missing return statement" error

}
