package com.rbndjx.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "timeline_event")
public class TimelineEvent extends PanacheEntityBase {

    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "timestamp", nullable = false)
    public LocalDateTime timestamp;

    @Column(name = "title", length = 500, nullable = false)
    public String title;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    public String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20, nullable = false)
    public TimelineEventType type;

    @Column(name = "location", length = 255)
    public String location;

    @Column(name = "image", columnDefinition = "TEXT", nullable = false)
    public String image;

    @Column(name = "created_at", nullable = false)
    public LocalDateTime createdAt;

    public TimelineEvent() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    public TimelineEvent(LocalDateTime timestamp, String title, String description, 
                         TimelineEventType type, String image, String location) {
        this();
        this.timestamp = timestamp;
        this.title = title;
        this.description = description;
        this.type = type;
        this.image = image;
        this.location = location;
    }
}
