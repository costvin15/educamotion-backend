package com.viniciuscastro.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.viniciuscastro.models.classroom.Classroom;

import io.quarkus.websockets.next.UserData.TypedKey;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ClassroomAttendanceService {
    Map<String, List<WebSocketConnection>> connections = new ConcurrentHashMap<>();

    @Inject
    ClassroomService classroomService;

    public void addAttendance(String classroomId, String userId, WebSocketConnection connection) {
        Optional<Classroom> classroom = this.classroomService.getClassroomByClassroomId(classroomId);
        if (!classroom.isPresent()) {
            throw new RuntimeException("Classroom does not exist");
        }

        if (!connections.containsKey(classroomId)) {
            connections.put(classroomId, new ArrayList<>());
        }

        connection.userData().put(TypedKey.forString("userId"), userId);
        connections.get(classroomId).add(connection);
    }

    public void removeAttendance(String classroomId, WebSocketConnection connection) {
        Optional<Classroom> classroom = this.classroomService.getClassroomByClassroomId(classroomId);
        if (!classroom.isPresent()) {
            throw new RuntimeException("Classroom does not exist");
        }

        if (connections.containsKey(classroomId)) {
            connections.get(classroomId).remove(connection);
        }
    }

    public List<WebSocketConnection> getClassroomAttendances(String classroomId) {
        if (!connections.containsKey(classroomId)) {
            return new ArrayList<>();
        }
        return connections.get(classroomId);
    }
}
