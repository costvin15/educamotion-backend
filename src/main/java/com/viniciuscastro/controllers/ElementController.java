package com.viniciuscastro.controllers;

import com.viniciuscastro.dto.request.CreateElementRequest;
import com.viniciuscastro.dto.request.UpdateElementRequest;
import com.viniciuscastro.dto.response.ElementResponse;
import com.viniciuscastro.services.ElementService;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
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

    @PUT
    @Path("update")
    public ElementResponse updateElement(UpdateElementRequest elementRequest) {
        return this.elementService.updateElement(
            elementRequest.getElementId(),
            elementRequest.getPositionX(),
            elementRequest.getPositionY(),
            elementRequest.getWidth(),
            elementRequest.getHeight()
        );
    }
}
