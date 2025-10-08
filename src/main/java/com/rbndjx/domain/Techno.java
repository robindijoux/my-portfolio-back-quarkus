package com.rbndjx.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "techno")
public class Techno extends PanacheEntityBase {

    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "technology", length = 255, nullable = false)
    public String technology;

    @Column(name = "icon_url", columnDefinition = "TEXT", nullable = false)
    public String iconUrl;

    @ManyToMany(mappedBy = "techStack")
    public List<Project> projects = new ArrayList<>();

    public Techno() {
        this.id = UUID.randomUUID().toString();
    }

    public Techno(String technology, String iconUrl) {
        this();
        this.technology = technology;
        this.iconUrl = iconUrl;
    }
}
