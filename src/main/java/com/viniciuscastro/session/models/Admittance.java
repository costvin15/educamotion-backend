package com.viniciuscastro.session.models;

import java.util.UUID;

import com.google.cloud.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Admittance {
    private String id;
    private String userId;
    private String sessionId;
    private Timestamp createdAt;

    public Admittance(String userId, String sessionId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.sessionId = sessionId;
        this.createdAt = Timestamp.now();
    }
}
