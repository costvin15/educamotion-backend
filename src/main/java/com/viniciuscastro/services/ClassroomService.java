package com.viniciuscastro.services;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;

import com.viniciuscastro.dto.response.UserAttendanceResponse;
import com.viniciuscastro.models.classroom.Classroom;
import com.viniciuscastro.models.presentations.Presentation;
import com.viniciuscastro.repositories.ClassroomRepository;

import io.quarkus.cache.CacheResult;
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

    @Inject
    Keycloak keycloak;

    public Optional<Classroom> getClassroomByPresentationId(String presentationId) {
        return repository.findByUserIdAndPresentationId(userService.getUserId(), presentationId);
    }

    public Optional<Classroom> getClassroomByClassroomId(String classroomId) {
        return repository.findByClassroomId(classroomId);
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

        Optional<Classroom> createdClassroom = this.getClassroomByPresentationId(presentationId);
        if (!createdClassroom.isPresent()) {
            throw new RuntimeException("Error creating classroom");
        }
        return createdClassroom.get();
    }

    @CacheResult(cacheName = "get-user-information")
    public UserAttendanceResponse getUserInformation(String userId) {
        UserRepresentation user = keycloak.realm("educamotion").users().get(userId).toRepresentation();
        return new UserAttendanceResponse(
            user.getId(),
            user.getFirstName() + " " + user.getLastName(),
            user.firstAttribute("picture")
        );
    }
}
