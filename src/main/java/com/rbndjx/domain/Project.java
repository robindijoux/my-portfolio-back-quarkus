package com.rbndjx.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "project")
public class Project extends PanacheEntityBase {

    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "name", length = 500, nullable = false)
    public String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    public String description;

    @Column(name = "short_description", length = 255, nullable = false)
    public String shortDescription;

    @Column(name = "repository_link")
    public String repositoryLink;

    @Column(name = "project_link")
    public String projectLink;

    @Column(name = "is_published", nullable = false)
    public Boolean isPublished = false;

    @Column(name = "featured", nullable = false)
    public Boolean featured = false;

    @Column(name = "views", nullable = false)
    public Integer views = 0;

    @Column(name = "created_at", nullable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    public List<Media> media = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
        name = "project_techno",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "techno_id")
    )
    public List<Techno> techStack = new ArrayList<>();

    public Project() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Project(String name, String description, String shortDescription, 
                   String repositoryLink, String projectLink, Boolean isPublished, Boolean featured) {
        this();
        this.name = name;
        this.description = description;
        this.shortDescription = shortDescription;
        this.repositoryLink = repositoryLink;
        this.projectLink = projectLink;
        this.isPublished = isPublished;
        this.featured = featured;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addMedia(Media mediaItem) {
        if (!this.media.contains(mediaItem)) {
            this.media.add(mediaItem);
            mediaItem.project = this;
        }
    }

    public void removeMedia(Media mediaItem) {
        this.media.remove(mediaItem);
        mediaItem.project = null;
    }

    public void addTechno(Techno techno) {
        if (!this.techStack.contains(techno)) {
            this.techStack.add(techno);
            techno.projects.add(this);
        }
    }

    public void removeTechno(Techno techno) {
        this.techStack.remove(techno);
        techno.projects.remove(this);
    }
}
