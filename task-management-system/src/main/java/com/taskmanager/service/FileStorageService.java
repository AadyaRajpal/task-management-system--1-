package com.taskmanager.service;

import com.taskmanager.model.Attachment;
import com.taskmanager.model.Project;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.repository.AttachmentRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public Attachment uploadFile(Long taskId, MultipartFile file, String username) {
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

        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = "";
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = originalFilename.substring(dotIndex);
            }
            String fileName = UUID.randomUUID().toString() + fileExtension;

            // Save file
            Path targetLocation = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Create attachment record
            Attachment attachment = new Attachment();
            attachment.setFileName(originalFilename);
            attachment.setFilePath(fileName);
            attachment.setFileType(file.getContentType());
            attachment.setFileSize(file.getSize());
            attachment.setTask(task);
            attachment.setUploadedBy(user);

            return attachmentRepository.save(attachment);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }
    }

    public Resource loadFileAsResource(Long attachmentId, String username) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check access
        Task task = attachment.getTask();
        Project project = task.getProject();
        if (!project.getOwner().getId().equals(user.getId()) && 
            !project.getMembers().contains(user)) {
            throw new RuntimeException("Access denied");
        }

        try {
            Path filePath = Paths.get(uploadDir).resolve(attachment.getFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found: " + attachment.getFileName());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found: " + attachment.getFileName());
        }
    }

    public List<Attachment> getTaskAttachments(Long taskId, String username) {
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

        return attachmentRepository.findByTask(task);
    }

    @Transactional
    public void deleteAttachment(Long id, String username) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!attachment.getUploadedBy().getId().equals(user.getId())) {
            throw new RuntimeException("Only uploader can delete the attachment");
        }

        // Delete file from filesystem
        try {
            Path filePath = Paths.get(uploadDir).resolve(attachment.getFilePath()).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage());
        }

        attachmentRepository.delete(attachment);
    }
}
