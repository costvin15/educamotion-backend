package com.viniciuscastro.repositories;

import java.util.Optional;

import com.viniciuscastro.models.classroom.Classroom;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class ClassroomRepository implements PanacheRepository<Classroom> {
    public Optional<Classroom> findByUserIdAndPresentationId(String userId, String presentationId) {
        return find("userId = ?1 and presentation.id = ?2 and active = true", userId, presentationId)
            .firstResultOptional();
    }
}
