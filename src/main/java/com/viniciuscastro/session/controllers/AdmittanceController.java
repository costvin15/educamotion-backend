package com.viniciuscastro.session.controllers;

import com.viniciuscastro.session.dto.requests.SubmitAdmittanceRequest;
import com.viniciuscastro.session.dto.responses.AdmittanceResponse;
import com.viniciuscastro.session.services.AdmittanceService;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Authenticated
@Path("session/admittance")
public class AdmittanceController {
    @Inject
    AdmittanceService service;

    @POST
    @Path("submit")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<AdmittanceResponse> registerAdmittance(SubmitAdmittanceRequest request) {
        return service.registerAdmittance(request);
    }
}
