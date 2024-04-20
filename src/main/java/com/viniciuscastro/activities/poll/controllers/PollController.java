package com.viniciuscastro.activities.poll.controllers;

import com.viniciuscastro.activities.poll.dto.requests.PollRequest;
import com.viniciuscastro.activities.poll.dto.responses.PollResponse;
import com.viniciuscastro.activities.poll.services.PollService;
import com.viniciuscastro.activity.dto.responses.ActivityResponse;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Authenticated
@Path("activity/poll")
public class PollController {
    @Inject
    PollService pollService;

    @GET
    @Path("{activityId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<PollResponse> findPollByActivityId(@PathParam("activityId") String activityId) {
        return pollService.findPollByActivityId(activityId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ActivityResponse> storePoll(PollRequest request) {
        return pollService.validateAndStorePoll(
            request.getPresentationId(),
            request.getQuestion(),
            request.getChoices()
        );
    }
}
