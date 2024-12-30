package com.viniciuscastro.repositories;

import java.util.List;
import java.util.Optional;

import com.viniciuscastro.models.presentations.Presentation;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class PresentationRepository implements PanacheRepository<Presentation> {
    public Optional<Presentation> findById(String id, String user) {
        return find("id = ?1 and user_id = ?2", id, user)
            .firstResultOptional();
    }

    public List<Presentation> findByUserId(String userId) {
        return find("user_id", userId)
            .list();
    }
}
