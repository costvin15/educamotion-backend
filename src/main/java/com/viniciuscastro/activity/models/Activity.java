package com.viniciuscastro.activity.models;

import java.util.UUID;

import com.google.cloud.Timestamp;
import com.viniciuscastro.activity.enums.ActivityType;

import lombok.Getter;

@Getter
public class Activity {
    private String id;
    private String presentationId;
    private ActivityType activityType;
    private String activityId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Activity(String presentationId, String activityId, ActivityType activityType) {
        this.id = UUID.randomUUID().toString();
        this.presentationId = presentationId;
        this.activityId = activityId;
        this.activityType = activityType;
        this.createdAt = Timestamp.now();
        this.updatedAt = Timestamp.now();
    }
}
