package com.viniciuscastro.slides.controllers;

import com.viniciuscastro.slides.models.Slide;
import com.viniciuscastro.slides.models.SlideThumbnail;
import com.viniciuscastro.slides.services.SlidesService;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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
    public Multi<Slide> findAll() {
        return this.slidesService.getSlides();
    }

    @GET
    @Path("thumbnail/{presentationId}/{pageObjectId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<SlideThumbnail> getThumbnail(@PathParam("presentationId") String presentationId, @PathParam("pageObjectId") String pageObjectId) {
        return this.slidesService.getThumbnail(presentationId, pageObjectId);
    }
}
