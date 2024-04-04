package com.viniciuscastro.activity.clients.requests;

import com.viniciuscastro.activity.enums.ActivityType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreActivityRequest {
    private String presentationId;
    private String activityId;
    private String objectId;
    private ActivityType activityType;
}
