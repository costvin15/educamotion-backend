package com.viniciuscastro.poll.controllers;

import com.viniciuscastro.activity.dto.responses.ActivityResponse;
import com.viniciuscastro.poll.dto.requests.PollRequest;
import com.viniciuscastro.poll.services.PollService;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Authenticated
@Path("poll")
public class PollController {
    @Inject
    PollService pollService;

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
