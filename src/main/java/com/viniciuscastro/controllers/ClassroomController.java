package com.viniciuscastro.controllers;

import java.util.Optional;

import com.viniciuscastro.dto.response.UserAttendanceResponse;
import com.viniciuscastro.models.classroom.Classroom;
import com.viniciuscastro.services.ClassroomService;
import com.viniciuscastro.services.UserService;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

@Path("classroom")
public class ClassroomController {
    @Inject
    ClassroomService classroomService;

    @Inject
    UserService userService;

    @GET
    @Authenticated
    @Path("{classroomId}")
    public Classroom getClassroom(String classroomId) {
        Optional<Classroom> classroom = this.classroomService.getClassroomByClassroomId(classroomId);
        if (classroom.isEmpty()) {
            throw new RuntimeException("Classroom not found");
        }
        return classroom.get();
    }

    @GET
    @Path("entry-code/{entryCode}")
    public Classroom getClassroomByEntryCode(String entryCode) {
        Optional<Classroom> classroom = this.classroomService.getClassroomByEntryCode(entryCode);
        if (classroom.isEmpty()) {
            throw new RuntimeException("Classroom not found");
        }
        return classroom.get();
    }

    @GET
    @Authenticated
    @Path("presentation/{presentationId}")
    public Classroom getClassroomByPresentationId(String presentationId) {
        Optional<Classroom> classroom = this.classroomService.getClassroomByPresentationId(presentationId);
        if (classroom.isEmpty()) {
            throw new RuntimeException("Classroom not found");
        }
        return classroom.get();
    }

    @POST
    @Authenticated
    @Path("create/{presentationId}")
    public Classroom createClassroom(String presentationId) {
        return this.classroomService.createClassroom(presentationId);
    }

    @POST
    @Authenticated
    @Path("close/{classroomId}")
    public Classroom closeClassroom(String classroomId) {
        return this.classroomService.closeClassroom(classroomId);
    }

    @PUT
    @Authenticated
    @Path("change-slide/{classroomId}/{slide}")
    public Classroom changeSlide(String classroomId, String slide) {
        return this.classroomService.changeSlide(classroomId, slide);
    }

    @GET
    @Authenticated
    @Path("user/{userId}")
    public UserAttendanceResponse getUserAttendance(String userId) {
        return this.classroomService.getUserInformation(userId);
    }
}
