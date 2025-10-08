package com.rbndjx.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Technology data transfer object")
public class TechnoDTO {
    
    @Schema(description = "Unique technology identifier")
    public String id;
    
    @Schema(description = "Technology name")
    public String technology;
    
    @Schema(description = "Technology icon URL")
    public String iconUrl;

    public TechnoDTO() {}

    public TechnoDTO(String id, String technology, String iconUrl) {
        this.id = id;
        this.technology = technology;
        this.iconUrl = iconUrl;
    }
}
