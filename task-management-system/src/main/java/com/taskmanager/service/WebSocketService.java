package com.taskmanager.service;

import com.taskmanager.model.Comment;
import com.taskmanager.model.Notification;
import com.taskmanager.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String username, Notification notification) {
        messagingTemplate.convertAndSendToUser(
            username,
            "/queue/notifications",
            notification
        );
    }

    public void notifyTaskCreated(Task task) {
        messagingTemplate.convertAndSend(
            "/topic/project/" + task.getProject().getId() + "/tasks",
            task
        );
    }

    public void notifyTaskUpdated(Task task) {
        messagingTemplate.convertAndSend(
            "/topic/project/" + task.getProject().getId() + "/tasks/update",
            task
        );
    }

    public void notifyTaskDeleted(Long taskId) {
        messagingTemplate.convertAndSend(
            "/topic/tasks/delete",
            taskId
        );
    }

    public void notifyNewComment(Comment comment) {
        messagingTemplate.convertAndSend(
            "/topic/task/" + comment.getTask().getId() + "/comments",
            comment
        );
    }
}
