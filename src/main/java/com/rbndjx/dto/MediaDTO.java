package com.rbndjx.dto;

import com.rbndjx.domain.MediaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Media data transfer object")
public class MediaDTO {
    
    @Schema(description = "Unique media identifier")
    public String id;
    
    @Schema(description = "Media type", enumeration = {"PHOTO", "VIDEO", "PDF", "DOCUMENT"})
    public MediaType type;
    
    @Schema(description = "Media URL")
    public String url;
    
    @Schema(description = "Alternative text for the media")
    public String alt;
    
    @Schema(description = "Original file name")
    public String originalName;
    
    @Schema(description = "Generated file name on server")
    public String fileName;
    
    @Schema(description = "MIME type of the file")
    public String mimeType;
    
    @Schema(description = "File size in bytes")
    public Long size;
    
    @Schema(description = "Upload timestamp")
    public LocalDateTime uploadedAt;
    
    @Schema(description = "User who uploaded the file")
    public String uploadedBy;

    public MediaDTO() {}

    public MediaDTO(String id, MediaType type, String url, String alt, String originalName, 
                    String fileName, String mimeType, Long size, LocalDateTime uploadedAt, String uploadedBy) {
        this.id = id;
        this.type = type;
        this.url = url;
        this.alt = alt;
        this.originalName = originalName;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.size = size;
        this.uploadedAt = uploadedAt;
        this.uploadedBy = uploadedBy;
    }
}
