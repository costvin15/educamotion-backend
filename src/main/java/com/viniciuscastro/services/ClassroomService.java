package com.viniciuscastro.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;

import com.viniciuscastro.dto.response.UserAttendanceResponse;
import com.viniciuscastro.models.classroom.Classroom;
import com.viniciuscastro.models.presentations.Presentation;
import com.viniciuscastro.repositories.ClassroomRepository;

import io.quarkus.logging.Log;
import io.quarkus.websockets.next.WebSocketConnection;
import io.quarkus.websockets.next.UserData.TypedKey;
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
    ClassroomAttendanceService classroomAttendanceService;

    @Inject
    Keycloak keycloak;

    public Optional<Classroom> getClassroomByPresentationId(String presentationId) {
        return repository.findByUserIdAndPresentationId(userService.getUserId(), presentationId);
    }

    public Optional<Classroom> getClassroomByClassroomId(String classroomId) {
        return repository.findByUserIdAndClassroomId(userService.getUserId(), classroomId);
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

    public List<UserAttendanceResponse> getAttendances(String classroomId) {
        Optional<Classroom> classroom = this.getClassroomByClassroomId(classroomId);
        if (!classroom.isPresent()) {
            throw new RuntimeException("Classroom does not exist");
        }

        Log.info(this.classroomAttendanceService.getClassroomAttendances(classroomId).size());

        List<UserAttendanceResponse> attendances = new ArrayList<>();
        for (WebSocketConnection connection : this.classroomAttendanceService.getClassroomAttendances(classroomId)) {
            String userId = connection.userData().get(TypedKey.forString("userId"));
            UserRepresentation user = keycloak.realm("educamotion").users().get(userId).toRepresentation();
            UserAttendanceResponse attendance = new UserAttendanceResponse(user.getId(), user.getFirstName() + " " + user.getLastName(), user.firstAttribute("picture"));
            attendances.add(attendance);
        }

        return attendances;
    }
}
