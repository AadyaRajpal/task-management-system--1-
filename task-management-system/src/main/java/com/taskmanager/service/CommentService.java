package com.taskmanager.service;

import com.taskmanager.dto.CommentRequest;
import com.taskmanager.model.Comment;
import com.taskmanager.model.Project;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.repository.CommentRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final WebSocketService webSocketService;

    @Transactional
    public Comment addComment(Long taskId, CommentRequest request, String username) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check access
        Project project = task.getProject();
        if (!project.getOwner().getId().equals(author.getId()) && 
            !project.getMembers().contains(author)) {
            throw new RuntimeException("Access denied");
        }

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setTask(task);
        comment.setAuthor(author);

        Comment savedComment = commentRepository.save(comment);
        
        // Notify task assignee and reporter
        if (task.getAssignee() != null && !task.getAssignee().getId().equals(author.getId())) {
            notificationService.createNotification(
                task.getAssignee(),
                "New Comment",
                author.getUsername() + " commented on task: " + task.getTitle(),
                "TASK_COMMENTED",
                task.getId()
            );
        }
        
        if (task.getReporter() != null && !task.getReporter().getId().equals(author.getId()) &&
            (task.getAssignee() == null || !task.getReporter().getId().equals(task.getAssignee().getId()))) {
            notificationService.createNotification(
                task.getReporter(),
                "New Comment",
                author.getUsername() + " commented on task: " + task.getTitle(),
                "TASK_COMMENTED",
                task.getId()
            );
        }
        
        // Send WebSocket notification
        webSocketService.notifyNewComment(savedComment);
        
        return savedComment;
    }

    public List<Comment> getTaskComments(Long taskId, String username) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check access
        Project project = task.getProject();
        if (!project.getOwner().getId().equals(user.getId()) && 
            !project.getMembers().contains(user)) {
            throw new RuntimeException("Access denied");
        }

        return commentRepository.findByTaskOrderByCreatedAtDesc(task);
    }

    @Transactional
    public void deleteComment(Long id, String username) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("Only comment author can delete the comment");
        }

        commentRepository.delete(comment);
    }
}
