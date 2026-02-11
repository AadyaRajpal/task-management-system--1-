package com.taskmanager.dto;

import com.taskmanager.model.Project;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {
    
    @NotBlank(message = "Project name is required")
    private String name;
    
    private String description;
    private Project.ProjectStatus status;
    private Set<Long> memberIds;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
