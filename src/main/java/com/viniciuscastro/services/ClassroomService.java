package com.viniciuscastro.services;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import com.viniciuscastro.models.classroom.Classroom;
import com.viniciuscastro.models.presentations.Presentation;
import com.viniciuscastro.repositories.ClassroomRepository;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class ClassroomService {
    @Inject
    ClassroomRepository repository;

    @Inject
    UserService userService;

    @Inject
    PresentationService presentationService;

    public Classroom getClassroom(String presentationId) {
        return repository.findByUserIdAndPresentationId(userService.getUserId(), presentationId)
            .orElseThrow(() -> new RuntimeException("Classroom does not exist"));
    }

    @Transactional
    public Classroom createClassroom(String presentationId) {
        if (repository.findByUserIdAndPresentationId(userService.getUserId(), presentationId).isPresent()) {
            throw new RuntimeException("Classroom already exists");
        }

        if (!presentationService.verifyIfPresentationExists(presentationId)) {
            throw new RuntimeException("Presentation does not exist");
        }

        Presentation presentation = presentationService.getPresentation(presentationId);
        Classroom classroom = new Classroom(
            UUID.randomUUID().toString(),
            userService.getUserId(),
            presentation,
            true,
            Date.from(Instant.now()),
            Date.from(Instant.now()),
            null
        );

        this.repository.persist(classroom);

        return this.getClassroom(presentationId);
    }
}
