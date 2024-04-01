package com.viniciuscastro.poll.controllers;

import com.viniciuscastro.poll.models.Poll;
import com.viniciuscastro.poll.services.PollService;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Authenticated
@Path("poll")
public class PollController {
    @Inject
    PollService pollService;

    @GET
    @Path("storePoll")
    public Poll storePoll() {
        return pollService.storePoll();
    }
}
