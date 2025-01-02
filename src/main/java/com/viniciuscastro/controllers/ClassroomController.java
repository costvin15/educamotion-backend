package com.viniciuscastro.controllers;

import java.util.List;
import java.util.Optional;

import com.viniciuscastro.dto.response.UserAttendanceResponse;
import com.viniciuscastro.models.classroom.Classroom;
import com.viniciuscastro.services.ClassroomAttendanceService;
import com.viniciuscastro.services.ClassroomService;
import com.viniciuscastro.services.UserService;

import io.quarkus.security.Authenticated;
import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnError;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Authenticated
@Path("classroom")
@WebSocket(path = "/classroom-controller/{classroomId}")
public class ClassroomController {
    @Inject
    ClassroomService classroomService;

    @Inject
    ClassroomAttendanceService classroomAttendanceService;

    @Inject
    UserService userService;

    @GET
    @Path("get/{classroomId}")
    public Classroom getClassroom(String classroomId) {
        Optional<Classroom> classroom = this.classroomService.getClassroomByClassroomId(classroomId);
        if (classroom.isPresent()) {
            return classroom.get();
        }
        return null;
    }

    @GET
    @Path("attendances/{classroomId}")
    public List<UserAttendanceResponse> getAttendances(String classroomId) {
        return this.classroomService.getAttendances(classroomId);
    }

    @POST
    @Path("create/{presentationId}")
    public Classroom createClassroom(String presentationId) {
        return this.classroomService.createClassroom(presentationId);
    }

    @OnOpen()
    public void onOpen(WebSocketConnection connection) {
        String classroomId = connection.pathParam("classroomId");
        String userId = this.userService.getUserId();
        this.classroomAttendanceService.addAttendance(classroomId, userId, connection);
    }

    @OnClose
    public void onClose(WebSocketConnection connection) {
        String classroomId = connection.pathParam("classroomId");
        this.classroomAttendanceService.removeAttendance(classroomId, connection);
        connection.close();
    }

    @OnError
    public void onError(Throwable throwable, WebSocketConnection connection) {
        String classroomId = connection.pathParam("classroomId");
        this.classroomAttendanceService.removeAttendance(classroomId, connection);
    }
}
