package com.viniciuscastro.activity.services;

import com.viniciuscastro.activity.clients.ActivityFirestoreResource;
import com.viniciuscastro.activity.clients.requests.StoreActivityRequest;
import com.viniciuscastro.activity.dto.responses.ActivityResponse;
import com.viniciuscastro.activity.enums.ActivityType;
import com.viniciuscastro.activity.models.Activity;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class ActivityService {
    @Inject
    ActivityFirestoreResource activityFirestoreResource;

    public ActivityResponse storeActivity(String presentationId, String activityId, String activityType) {
        StoreActivityRequest storeActivityRequest = new StoreActivityRequest(presentationId, activityId, ActivityType.valueOf(activityType));
        Activity activity = activityFirestoreResource.storeActivity(storeActivityRequest);
        return new ActivityResponse(
            activity.getId(),
            activity.getPresentationId(),
            activity.getActivityId(),
            activity.getActivityType().name(),
            activity.getCreatedAt().toDate(),
            activity.getUpdatedAt().toDate()
        );
    }
}
