package com.taskmanager.controller;

import com.taskmanager.model.Attachment;
import com.taskmanager.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("/task/{taskId}")
    public ResponseEntity<Attachment> uploadFile(
            @PathVariable Long taskId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        Attachment attachment = fileStorageService.uploadFile(taskId, file, authentication.getName());
        return new ResponseEntity<>(attachment, HttpStatus.CREATED);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Attachment>> getTaskAttachments(
            @PathVariable Long taskId,
            Authentication authentication) {
        List<Attachment> attachments = fileStorageService.getTaskAttachments(taskId, authentication.getName());
        return ResponseEntity.ok(attachments);
    }

    @GetMapping("/download/{attachmentId}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long attachmentId,
            Authentication authentication) {
        Resource resource = fileStorageService.loadFileAsResource(attachmentId, authentication.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttachment(
            @PathVariable Long id,
            Authentication authentication) {
        fileStorageService.deleteAttachment(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
