package com.viniciuscastro.slides.controllers;

import java.util.List;

import com.viniciuscastro.slides.models.Slide;
import com.viniciuscastro.slides.services.SlidesService;

import io.quarkus.security.Authenticated;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("slides")
@Authenticated
public class SlidesController {
    @Inject
    SlidesService slidesService;

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @Blocking
    public Uni<List<Slide>> findAll() {
        return this.slidesService.getSlides();
    }
}
