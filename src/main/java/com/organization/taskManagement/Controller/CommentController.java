package com.organization.taskManagement.Controller;

import com.organization.taskManagement.DTO.Request.CommentRequestDTO;
import com.organization.taskManagement.DTO.Response.ApiResponseDTO;
import com.organization.taskManagement.DTO.Response.CommentResponseDTO;
import com.organization.taskManagement.Services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{id}/comments")
    public ResponseEntity<ApiResponseDTO<CommentResponseDTO>> createComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDTO request
    ) {
        try {
            CommentResponseDTO response = commentService.createComment(id , request);
            return ResponseEntity.ok(ApiResponseDTO.success("Comment added successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.failure(e.getMessage()));
        }

    }
}