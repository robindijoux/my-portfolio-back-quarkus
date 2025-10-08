package com.rbndjx.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Add media to project request")
public class AddMediaToProjectDTO {
    
    @NotBlank
    @Schema(description = "ID of previously uploaded media to associate with the project", 
            required = true,
            example = "123e4567-e89b-12d3-a456-426614174000")
    public String mediaId;

    public AddMediaToProjectDTO() {}

    public AddMediaToProjectDTO(String mediaId) {
        this.mediaId = mediaId;
    }
}
