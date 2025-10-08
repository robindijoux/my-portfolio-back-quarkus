package com.rbndjx.domain;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MediaRepository implements PanacheRepositoryBase<Media, String> {
    // Méthodes CRUD de base fournies par Panache
}
