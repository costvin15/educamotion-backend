package com.viniciuscastro.controllers;

import com.viniciuscastro.dto.request.CreateElementRequest;
import com.viniciuscastro.dto.response.ElementResponse;
import com.viniciuscastro.services.ElementService;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Authenticated
@Path("element")
public class ElementController {
    @Inject
    ElementService elementService;

    @POST
    @Path("create")
    public ElementResponse createElement(CreateElementRequest elementRequest) {
        return this.elementService.createElement(
            elementRequest.getPresentationId(),
            elementRequest.getSlideId(),
            elementRequest.getElementType().toUpperCase()
        );
    }
}
