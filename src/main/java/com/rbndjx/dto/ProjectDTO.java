package com.rbndjx.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Project data transfer object")
public class ProjectDTO {
    
    @Schema(description = "Unique project identifier")
    public String id;
    
    @Schema(description = "Project name")
    public String name;
    
    @Schema(description = "Detailed project description")
    public String description;
    
    @Schema(description = "Short project description")
    public String shortDescription;
    
    @Schema(description = "Indicates if the project is published")
    public Boolean isPublished;
    
    @Schema(description = "Indicates if the project is featured")
    public Boolean featured;
    
    @Schema(description = "Number of project views")
    public Integer views;
    
    @Schema(description = "Repository link")
    public String repositoryLink;
    
    @Schema(description = "Online project link")
    public String projectLink;
    
    @Schema(description = "Project creation date")
    public LocalDateTime createdAt;
    
    @Schema(description = "Array of media objects associated with the project")
    public List<MediaDTO> media = new ArrayList<>();
    
    @Schema(description = "Technologies used in the project")
    public List<TechnoDTO> techStack = new ArrayList<>();

    public ProjectDTO() {}

    public ProjectDTO(String id, String name, String description, String shortDescription, 
                      Boolean isPublished, Boolean featured, Integer views, String repositoryLink, 
                      String projectLink, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.shortDescription = shortDescription;
        this.isPublished = isPublished;
        this.featured = featured;
        this.views = views;
        this.repositoryLink = repositoryLink;
        this.projectLink = projectLink;
        this.createdAt = createdAt;
    }
}
