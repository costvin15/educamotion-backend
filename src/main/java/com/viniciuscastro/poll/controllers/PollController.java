package com.viniciuscastro.poll.controllers;

import com.viniciuscastro.poll.dto.requests.PollRequest;
import com.viniciuscastro.poll.dto.responses.PollResponse;
import com.viniciuscastro.poll.services.PollService;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Authenticated
@Path("poll")
public class PollController {
    @Inject
    PollService pollService;

    @POST
    public PollResponse storePoll(PollRequest request) {
        return pollService.storePoll(
            request.getPresentationId(),
            request.getQuestion(),
            request.getChoices()
        );
    }
}
