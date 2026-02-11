package com.taskmanager.service;

import com.taskmanager.dto.ProjectRequest;
import com.taskmanager.model.Project;
import com.taskmanager.model.User;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public Project createProject(ProjectRequest request, String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus() != null ? request.getStatus() : Project.ProjectStatus.ACTIVE);
        project.setOwner(owner);
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());

        if (request.getMemberIds() != null && !request.getMemberIds().isEmpty()) {
            Set<User> members = new HashSet<>(userRepository.findAllById(request.getMemberIds()));
            project.setMembers(members);
            
            // Notify members
            for (User member : members) {
                notificationService.createNotification(
                    member,
                    "Added to Project",
                    "You have been added to project: " + project.getName(),
                    "PROJECT_ADDED",
                    project.getId()
                );
            }
        }

        return projectRepository.save(project);
    }

    @Transactional
    public Project updateProject(Long id, ProjectRequest request, String username) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!project.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Only project owner can update the project");
        }

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());

        if (request.getMemberIds() != null) {
            Set<User> newMembers = new HashSet<>(userRepository.findAllById(request.getMemberIds()));
            
            // Notify newly added members
            Set<User> currentMembers = project.getMembers();
            for (User newMember : newMembers) {
                if (!currentMembers.contains(newMember)) {
                    notificationService.createNotification(
                        newMember,
                        "Added to Project",
                        "You have been added to project: " + project.getName(),
                        "PROJECT_ADDED",
                        project.getId()
                    );
                }
            }
            
            project.setMembers(newMembers);
        }

        return projectRepository.save(project);
    }

    public List<Project> getUserProjects(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return projectRepository.findByOwnerOrMember(user);
    }

    public Project getProjectById(Long id, String username) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!project.getOwner().getId().equals(user.getId()) && 
            !project.getMembers().contains(user)) {
            throw new RuntimeException("Access denied");
        }

        return project;
    }

    @Transactional
    public void deleteProject(Long id, String username) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!project.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Only project owner can delete the project");
        }

        projectRepository.delete(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
}
