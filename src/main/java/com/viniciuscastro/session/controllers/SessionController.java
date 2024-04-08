package com.viniciuscastro.session.controllers;

import com.viniciuscastro.session.dto.requests.CreateSessionRequest;
import com.viniciuscastro.session.dto.responses.SessionResponse;
import com.viniciuscastro.session.services.SessionService;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Authenticated
@Path("session")
public class SessionController {
    @Inject
    SessionService service;

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<SessionResponse> createSession(CreateSessionRequest request) {
        return service.createSession(request);
    }
}
