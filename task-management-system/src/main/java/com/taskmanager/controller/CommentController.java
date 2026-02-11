package com.taskmanager.controller;

import com.taskmanager.dto.CommentRequest;
import com.taskmanager.model.Comment;
import com.taskmanager.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/task/{taskId}")
    public ResponseEntity<Comment> addComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication) {
        Comment comment = commentService.addComment(taskId, request, authentication.getName());
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Comment>> getTaskComments(
            @PathVariable Long taskId,
            Authentication authentication) {
        List<Comment> comments = commentService.getTaskComments(taskId, authentication.getName());
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            Authentication authentication) {
        commentService.deleteComment(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
