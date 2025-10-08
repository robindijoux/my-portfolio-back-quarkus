package com.rbndjx.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Simple technology DTO for creation")
public class SimpleTechnoDTO {
    
    @NotBlank
    @Schema(description = "Technology name", required = true)
    public String technology;
    
    @NotBlank
    @Schema(description = "Technology icon URL", required = true)
    public String iconUrl;

    public SimpleTechnoDTO() {}

    public SimpleTechnoDTO(String technology, String iconUrl) {
        this.technology = technology;
        this.iconUrl = iconUrl;
    }
}
