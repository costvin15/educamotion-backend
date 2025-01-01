package com.viniciuscastro.controllers;

import com.viniciuscastro.models.classroom.Classroom;
import com.viniciuscastro.services.ClassroomService;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Authenticated
@Path("classroom")
public class ClassroomController {
    @Inject
    ClassroomService service;

    @GET
    @Path("get/{presentationId}")
    public Classroom getClassroom(String presentationId) {
        return this.service.getClassroom(presentationId);
    }

    @POST
    @Path("create/{presentationId}")
    public Classroom createClassroom(String presentationId) {
        return this.service.createClassroom(presentationId);
    }
}
