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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final EmployeeRegisterRepository employeeRegisterRepository;
    private final ActivityLogService activityLogService;

    public CommentResponseDTO addComment(Long taskId, CommentRequestDTO requestDTO) {
        TaskModel task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        EmployeeRegisterModel employee = employeeRegisterRepository.findByEmployeeId(requestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + requestDTO.getEmployeeId()));

        CommentModel comment = CommentMapper.toEntity(requestDTO, task, employee);
        CommentModel savedComment = commentRepository.save(comment);
        
        activityLogService.logActivity("Comment Added", "Added comment to task: " + task.getTitle(), task);
        
        return CommentMapper.toResponse(savedComment);
    }

    public List<CommentResponseDTO> getCommentsByTaskId(Long taskId) {
        TaskModel task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
                
        return task.getComments().stream()
                .map(CommentMapper::toResponse)
                .collect(Collectors.toList());
    }
}
