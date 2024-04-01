package com.viniciuscastro.poll.models;

import java.util.UUID;

import com.google.cloud.Timestamp;

import lombok.Getter;

@Getter
public class Choice {
    private String id;
    private String description;
    private String pollId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Choice(String description, String pollId) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.pollId = pollId;
        this.createdAt = Timestamp.now();
        this.updatedAt = Timestamp.now();
    }
}
