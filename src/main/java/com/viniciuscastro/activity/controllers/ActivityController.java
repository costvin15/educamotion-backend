package com.viniciuscastro.activity.controllers;

import com.viniciuscastro.activity.dto.requests.ActivityRequest;
import com.viniciuscastro.activity.dto.responses.ActivitiesResponse;
import com.viniciuscastro.activity.dto.responses.ActivityResponse;
import com.viniciuscastro.activity.services.ActivityService;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Authenticated
@Path("activity")
public class ActivityController {
    @Inject
    ActivityService activityService;

    @GET
    @Path("{presentationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ActivitiesResponse> getActivitiesByPresentationId(String presentationId) {
        return activityService.getActivitiesByPresentationId(presentationId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ActivityResponse> storeActivity(ActivityRequest request) {
        return activityService.validateAndStoreActivity(
            request.getPresentationId(),
            request.getActivityId(),
            request.getType()
        );
    }
}
