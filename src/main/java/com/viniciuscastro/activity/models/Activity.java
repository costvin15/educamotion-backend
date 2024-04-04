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
    private String objectId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Activity(String presentationId, String activityId, ActivityType activityType, String objectId) {
        this.id = UUID.randomUUID().toString();
        this.presentationId = presentationId;
        this.activityId = activityId;
        this.activityType = activityType;
        this.objectId = objectId;
        this.createdAt = Timestamp.now();
        this.updatedAt = Timestamp.now();
    }

    public Activity(String id, String presentationId, String activityId, String activityType, String objectId, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.presentationId = presentationId;
        this.activityId = activityId;
        this.activityType = ActivityType.valueOf(activityType);
        this.objectId = objectId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
