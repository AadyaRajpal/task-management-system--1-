package com.taskmanager.service;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.model.Project;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final WebSocketService webSocketService;

    @Transactional
    public Task createTask(TaskRequest request, String username) {
        User reporter = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Check if user has access to project
        if (!project.getOwner().getId().equals(reporter.getId()) && 
            !project.getMembers().contains(reporter)) {
            throw new RuntimeException("Access denied to this project");
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setProject(project);
        task.setReporter(reporter);
        task.setStatus(request.getStatus() != null ? request.getStatus() : Task.TaskStatus.TODO);
        task.setPriority(request.getPriority() != null ? request.getPriority() : Task.TaskPriority.MEDIUM);
        task.setDueDate(request.getDueDate());

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            task.setAssignee(assignee);
            
            // Notify assignee
            notificationService.createNotification(
                assignee,
                "New Task Assigned",
                "Task '" + task.getTitle() + "' has been assigned to you",
                "TASK_ASSIGNED",
                task.getId()
            );
        }

        Task savedTask = taskRepository.save(task);
        
        // Send WebSocket notification
        webSocketService.notifyTaskCreated(savedTask);
        
        return savedTask;
    }

    @Transactional
    public Task updateTask(Long id, TaskRequest request, String username) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check access
        Project project = task.getProject();
        if (!project.getOwner().getId().equals(user.getId()) && 
            !project.getMembers().contains(user)) {
            throw new RuntimeException("Access denied");
        }

        User oldAssignee = task.getAssignee();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());

        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
            if (request.getStatus() == Task.TaskStatus.DONE && task.getCompletedAt() == null) {
                task.setCompletedAt(LocalDateTime.now());
            }
        }

        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }

        if (request.getAssigneeId() != null) {
            User newAssignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            
            if (oldAssignee == null || !oldAssignee.getId().equals(newAssignee.getId())) {
                task.setAssignee(newAssignee);
                
                notificationService.createNotification(
                    newAssignee,
                    "Task Assigned",
                    "Task '" + task.getTitle() + "' has been assigned to you",
                    "TASK_ASSIGNED",
                    task.getId()
                );
            }
        }

        Task updatedTask = taskRepository.save(task);
        
        // Send WebSocket notification
        webSocketService.notifyTaskUpdated(updatedTask);
        
        // Notify task participants
        if (task.getAssignee() != null && !task.getAssignee().getId().equals(user.getId())) {
            notificationService.createNotification(
                task.getAssignee(),
                "Task Updated",
                "Task '" + task.getTitle() + "' has been updated",
                "TASK_UPDATED",
                task.getId()
            );
        }

        return updatedTask;
    }

    public List<Task> getProjectTasks(Long projectId, String username) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!project.getOwner().getId().equals(user.getId()) && 
            !project.getMembers().contains(user)) {
            throw new RuntimeException("Access denied");
        }

        return taskRepository.findByProject(project);
    }

    public List<Task> getUserTasks(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return taskRepository.findByAssignee(user);
    }

    public Task getTaskById(Long id, String username) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = task.getProject();
        if (!project.getOwner().getId().equals(user.getId()) && 
            !project.getMembers().contains(user)) {
            throw new RuntimeException("Access denied");
        }

        return task;
    }

    @Transactional
    public void deleteTask(Long id, String username) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = task.getProject();
        if (!project.getOwner().getId().equals(user.getId()) && 
            !task.getReporter().getId().equals(user.getId())) {
            throw new RuntimeException("Only project owner or task reporter can delete the task");
        }

        taskRepository.delete(task);
        
        // Send WebSocket notification
        webSocketService.notifyTaskDeleted(id);
    }
}
