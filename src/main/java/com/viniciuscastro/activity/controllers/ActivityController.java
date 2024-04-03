package com.viniciuscastro.activity.controllers;

import com.viniciuscastro.activity.dto.requests.ActivityRequest;
import com.viniciuscastro.activity.dto.responses.ActivityResponse;
import com.viniciuscastro.activity.services.ActivityService;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Authenticated
@Path("activity")
public class ActivityController {
    @Inject
    ActivityService activityService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public ActivityResponse storeActivity(ActivityRequest request) {
        return activityService.storeActivity(
            request.getPresentationId(),
            request.getActivityId(),
            request.getType()
        );
    }
}
