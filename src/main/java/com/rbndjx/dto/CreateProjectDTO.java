package com.rbndjx.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Project creation data transfer object")
public class CreateProjectDTO {
    
    @NotBlank
    @Schema(description = "Project name", required = true)
    public String name;
    
    @NotBlank
    @Schema(description = "Detailed project description", required = true)
    public String description;
    
    @NotBlank
    @Schema(description = "Short project description", required = true)
    public String shortDescription;
    
    @NotNull
    @Schema(description = "Indicates if the project is published", required = true)
    public Boolean isPublished;
    
    @NotNull
    @Schema(description = "Indicates if the project is featured", required = true)
    public Boolean featured;
    
    @Schema(description = "Repository link")
    public String repositoryLink;
    
    @Schema(description = "Online project link")
    public String projectLink;
    
    @Schema(description = "Array of previously uploaded media IDs", 
            example = "[\"123e4567-e89b-12d3-a456-426614174000\"]")
    public List<String> media = new ArrayList<>();
    
    @Schema(description = "Technologies used in the project (will be created if they don't exist)")
    public List<SimpleTechnoDTO> techStack = new ArrayList<>();

    public CreateProjectDTO() {}
}
