package com.rbndjx.domain;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProjectRepository implements PanacheRepositoryBase<Project, String> {
    // Panache fournit déjà toutes les méthodes CRUD de base
    // Vous pouvez ajouter des requêtes personnalisées ici si nécessaire
    
    public Project findByName(String name) {
        return find("name", name).firstResult();
    }
}
