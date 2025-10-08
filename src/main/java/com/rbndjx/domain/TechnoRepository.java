package com.rbndjx.domain;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TechnoRepository implements PanacheRepositoryBase<Techno, String> {
    
    public Techno findByTechnology(String technology) {
        return find("technology", technology).firstResult();
    }
}
