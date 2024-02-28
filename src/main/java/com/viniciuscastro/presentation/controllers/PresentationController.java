package com.viniciuscastro.presentation.controllers;

import com.viniciuscastro.presentation.models.DrivePage;
import com.viniciuscastro.presentation.models.Presentation;
import com.viniciuscastro.presentation.models.PresentationThumbnail;
import com.viniciuscastro.presentation.services.PresentationService;

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

@Path("presentation")
@Authenticated
public class PresentationController {
    @Inject
    PresentationService slidesService;

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<DrivePage> findPage(@QueryParam("pageToken") String pageToken) {
        return this.slidesService.findPresentationsFromDrive(pageToken);
    }

    @GET
    @Path("{presentationId}")
    public Uni<Presentation> findPresentationInformation(@PathParam("presentationId") String presentationId) {
        return this.slidesService.findPresentationInformation(presentationId);
    }

    @GET
    @Path("thumbnail/{presentationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<PresentationThumbnail> getThumbnail(@PathParam("presentationId") String presentationId) {
        return this.slidesService.getThumbnail(presentationId);
    }

    @GET
    @Path("thumbnails/{presentationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<PresentationThumbnail> getThumbnails(@PathParam("presentationId") String presentationId) {
        return this.slidesService.getAllThumbnails(presentationId);
    }
}
