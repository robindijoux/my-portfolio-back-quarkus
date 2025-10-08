package com.rbndjx.dto;

import com.rbndjx.domain.TimelineEventType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Timeline event data transfer object")
public class TimelineEventDTO {
    
    @Schema(description = "Unique timeline event identifier")
    public String id;
    
    @NotNull
    @Schema(description = "Event timestamp", required = true)
    public LocalDateTime timestamp;
    
    @NotBlank
    @Schema(description = "Event title", required = true)
    public String title;
    
    @NotBlank
    @Schema(description = "Event description", required = true)
    public String description;
    
    @NotNull
    @Schema(description = "Event type (case-insensitive)", 
            enumeration = {"EDUCATION", "ACHIEVEMENT", "WORK", "education", "achievement", "work"}, 
            required = true,
            example = "ACHIEVEMENT")
    public TimelineEventType type;
    
    @Schema(description = "Event location")
    public String location;
    
    @NotBlank
    @Schema(description = "Event image URL", required = true)
    public String image;
    
    @Schema(description = "Event creation date")
    public LocalDateTime createdAt;

    public TimelineEventDTO() {}

    public TimelineEventDTO(String id, LocalDateTime timestamp, String title, String description, 
                            TimelineEventType type, String location, String image, LocalDateTime createdAt) {
        this.id = id;
        this.timestamp = timestamp;
        this.title = title;
        this.description = description;
        this.type = type;
        this.location = location;
        this.image = image;
        this.createdAt = createdAt;
    }
}
