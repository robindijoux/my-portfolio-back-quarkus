package com.rbndjx.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "media")
public class Media extends PanacheEntityBase {

    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 16, nullable = false)
    public MediaType type;

    @Column(name = "url", columnDefinition = "TEXT", nullable = false)
    public String url;

    @Column(name = "alt", length = 255)
    public String alt;

    @Column(name = "original_name", length = 255, nullable = false)
    public String originalName;

    @Column(name = "file_name", length = 255, nullable = false)
    public String fileName;

    @Column(name = "mime_type", length = 100, nullable = false)
    public String mimeType;

    @Column(name = "size", nullable = false)
    public Long size;

    @Column(name = "uploaded_at", nullable = false)
    public LocalDateTime uploadedAt;

    @Column(name = "uploaded_by", length = 255)
    public String uploadedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    public Project project;

    public Media() {
        this.id = UUID.randomUUID().toString();
        this.uploadedAt = LocalDateTime.now();
    }

    public Media(MediaType type, String url, String originalName, String fileName, 
                 String mimeType, Long size, String alt, String uploadedBy) {
        this();
        this.type = type;
        this.url = url;
        this.originalName = originalName;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.size = size;
        this.alt = alt;
        this.uploadedBy = uploadedBy;
    }

    public static MediaType determineTypeFromMimeType(String mimeType) {
        if (mimeType.startsWith("image/")) {
            return MediaType.PHOTO;
        } else if (mimeType.startsWith("video/")) {
            return MediaType.VIDEO;
        } else if (mimeType.equals("application/pdf")) {
            return MediaType.PDF;
        }
        return MediaType.DOCUMENT;
    }

    public String getFormattedSize() {
        String[] units = {"B", "KB", "MB", "GB"};
        double size = this.size;
        int unitIndex = 0;
        
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        
        return String.format("%.1f %s", size, units[unitIndex]);
    }
}
