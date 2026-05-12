package com.organization.taskManagement.Controller;

import com.organization.taskManagement.DTO.Request.CommentRequestDTO;
import com.organization.taskManagement.DTO.Response.ApiResponseDTO;
import com.organization.taskManagement.DTO.Response.CommentCreateResponse;
import com.organization.taskManagement.DTO.Response.CommentResponseDTO;
import com.organization.taskManagement.Services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //post comment  who is id comment the task
    @PostMapping("/{id}/comments")
    public ResponseEntity<ApiResponseDTO<CommentCreateResponse>> addComment(@PathVariable Long id, @RequestBody CommentRequestDTO requestDTO){
        CommentResponseDTO commentResponseDTO = commentService.addComment(id, requestDTO);
        CommentCreateResponse commentCreateResponse = new CommentCreateResponse(commentResponseDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Comment added successfully", commentCreateResponse));
    }
}
