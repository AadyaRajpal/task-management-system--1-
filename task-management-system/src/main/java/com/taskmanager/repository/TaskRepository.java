package com.taskmanager.repository;

import com.taskmanager.model.Project;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject(Project project);
    List<Task> findByAssignee(User assignee);
    List<Task> findByReporter(User reporter);
    List<Task> findByStatus(Task.TaskStatus status);
    List<Task> findByProjectAndStatus(Project project, Task.TaskStatus status);
    List<Task> findByAssigneeAndStatus(User assignee, Task.TaskStatus status);
}
