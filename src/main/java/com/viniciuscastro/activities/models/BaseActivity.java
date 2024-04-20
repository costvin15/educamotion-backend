package com.viniciuscastro.activities.models;

import com.google.cloud.Timestamp;

import lombok.Getter;

@Getter
public abstract class BaseActivity {
    protected String id;
    protected String presentationId;
    protected Timestamp createdAt;
    protected Timestamp updatedAt;

    public BaseActivity(String presentationId) {
        this.id = java.util.UUID.randomUUID().toString();
        this.presentationId = presentationId;
        this.createdAt = Timestamp.now();
        this.updatedAt = Timestamp.now();
    }
}
