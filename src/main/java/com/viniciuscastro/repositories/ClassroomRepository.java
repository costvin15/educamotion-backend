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

    public Optional<Classroom> findByUserIdAndClassroomId(String userId, String classroomId) {
        return find("userId = ?1 and id = ?2 and active = true", userId, classroomId)
            .firstResultOptional();
    }

    public Optional<Classroom> findByClassroomId(String classroomId) {
        return find("id = ?1 and active = true", classroomId)
            .firstResultOptional();
    }

    public Optional<Classroom> findByEntryCode(String entryCode) {
        return find("entryCode = ?1 and active = true", entryCode)
            .firstResultOptional();
    }

    public boolean existsByEntryCode(String entryCode) {
        return find("entryCode = ?1 and active = true", entryCode)
            .firstResultOptional()
            .isPresent();
    }
}
