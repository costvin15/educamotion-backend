package com.viniciuscastro.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.viniciuscastro.models.classroom.Classroom;
import com.viniciuscastro.services.ClassroomService;
import com.viniciuscastro.services.UserService;

import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;
import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnError;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import io.quarkus.websockets.next.UserData.TypedKey;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
@Authenticated
@Path("classroom")
@WebSocket(path = "/classroom-controller/{classroomId}")
public class ClassroomController {
    @Inject
    ClassroomService service;

    @Inject
    UserService userService;

    Map<String, List<WebSocketConnection>> connections = new ConcurrentHashMap<>();

    @GET
    @Path("get/{presentationId}")
    public Classroom getClassroom(String presentationId) {
        Optional<Classroom> classroom = this.service.getClassroomByPresentationId(presentationId);
        if (classroom.isPresent()) {
            return classroom.get();
        }
        return null;
    }

    @POST
    @Path("create/{presentationId}")
    public Classroom createClassroom(String presentationId) {
        return this.service.createClassroom(presentationId);
    }

    @OnOpen()
    public void onOpen(WebSocketConnection connection) {
        String classroomId = connection.pathParam("classroomId");
        Log.info("Connection opened with classroomId: " + classroomId);
        connection.userData().put(TypedKey.forString("userId"), userService.getUserId());

        if (!service.getClassroomByClassroomId(classroomId).isPresent()) {
            connection.closeAndAwait();
            return;
        }

        if (!connections.containsKey(classroomId)) {
            connections.put(classroomId, new ArrayList<>());
        }

        connections.get(classroomId).add(connection);
    }

    @OnClose
    public void onClose(WebSocketConnection connection) {
        String classroomId = connection.pathParam("classroomId");
        this.removeConnection(classroomId, connection);
        connection.close();
    }

    @OnError
    public void onError(Throwable throwable, WebSocketConnection connection) {
        String classroomId = connection.pathParam("classroomId");
        this.removeConnection(classroomId, connection);
    }

    private void removeConnection(String classroomId, WebSocketConnection connection) {
        if (!connections.containsKey(classroomId)) {
            return;
        }

        connections.get(classroomId).remove(connection);
    }
}
