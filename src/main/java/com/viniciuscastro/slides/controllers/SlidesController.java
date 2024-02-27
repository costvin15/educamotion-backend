package com.viniciuscastro.slides.controllers;

import com.viniciuscastro.slides.models.DrivePage;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("slides")
@Authenticated
public class SlidesController {
    @Inject
    SlidesService slidesService;

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<DrivePage> findPage(@QueryParam("pageToken") String pageToken) {
        return this.slidesService.findPresentationsFromDrive(pageToken);
    }

    @GET
    @Path("{presentationId}")
    public Uni<Slide> findSlideInformation(@PathParam("presentationId") String presentationId) {
        return this.slidesService.findSlideInformation(presentationId);
    }

    @GET
    @Path("thumbnail/{presentationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<SlideThumbnail> getThumbnail(@PathParam("presentationId") String presentationId) {
        return this.slidesService.getThumbnail(presentationId);
    }

    @GET
    @Path("thumbnails/{presentationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<SlideThumbnail> getThumbnails(@PathParam("presentationId") String presentationId) {
        return this.slidesService.getAllThumbnails(presentationId);
    }
}
