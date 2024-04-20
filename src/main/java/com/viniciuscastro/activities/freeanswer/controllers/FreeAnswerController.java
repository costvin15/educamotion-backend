package com.viniciuscastro.activities.freeanswer.controllers;

import com.viniciuscastro.activities.freeanswer.dto.requests.FreeAnswerRequest;
import com.viniciuscastro.activities.freeanswer.services.FreeAnswerService;
import com.viniciuscastro.activity.dto.responses.ActivityResponse;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Authenticated
@Path("activity/freeanswer")
public class FreeAnswerController {
    @Inject
    FreeAnswerService freeAnswerService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ActivityResponse> storeFreeAnswer(FreeAnswerRequest request) {
        return freeAnswerService.validateAndStoreFreeAnswer(
            request.getPresentationId(),
            request.getQuestion(),
            request.getMaxWords()
        );
    }
}
