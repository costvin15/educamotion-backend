package com.viniciuscastro.poll.models;

import java.util.UUID;

import com.google.cloud.Timestamp;

import lombok.Getter;

@Getter
public class Poll {
    private String id;
    private String question;
    private String presentationId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Poll(String question, String presentationId) {
        this.id = UUID.randomUUID().toString();
        this.question = question;
        this.presentationId = presentationId;
        this.createdAt = Timestamp.now();
        this.updatedAt = Timestamp.now();
    }
}
