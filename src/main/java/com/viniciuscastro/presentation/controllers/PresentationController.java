package com.viniciuscastro.presentation.controllers;

import java.io.ByteArrayInputStream;

import com.viniciuscastro.clients.models.responses.PresentationUpdateResponse;
import com.viniciuscastro.presentation.dto.request.ImportPresentation;
import com.viniciuscastro.presentation.dto.request.UpdateSlidePositions;
import com.viniciuscastro.presentation.dto.response.ImportResultResponse;
import com.viniciuscastro.presentation.dto.response.PresentationListResponse;
import com.viniciuscastro.presentation.dto.response.PresentationWithSlidesResponse;
import com.viniciuscastro.presentation.models.DrivePage;
import com.viniciuscastro.presentation.services.PresentationService;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Authenticated
@Path("presentation")
public class PresentationController {
    @Inject
    PresentationService service;

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<DrivePage> findPage(@QueryParam("pageToken") String pageToken) {
        return service.findPresentationsAvailableForImport(pageToken);
    }

    @POST
    @Path("import")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ImportResultResponse> importPresentations(ImportPresentation request) {
        return service.importPresentations(request.getPresentationIds());
    }

    @GET
    @Path("{presentationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<PresentationWithSlidesResponse> findPresentationInformation(@PathParam("presentationId") String presentationId) {
        return service.getPresentationById(presentationId);
    }

    @GET
    @Path("thumbnail/{presentationId}")
    @Produces("image/png")
    public Uni<ByteArrayInputStream> findThumbnail(@PathParam("presentationId") String presentationId) {
        return service.getThumbnail(presentationId);
    }

    @GET
    @Path("thumbnail/{presentationId}/{slideId}")
    @Produces("image/png")
    public Uni<ByteArrayInputStream> findSlideThumbnail(@PathParam("presentationId") String presentationId, @PathParam("slideId") String slideId) {
        return service.getSlideThumbnail(presentationId, slideId);
    }

    @GET
    @Path("imported")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<PresentationListResponse> findImportedPresentations() {
        return service.searchAllImportedPresentations();
    }

    @GET
    @Path("create/{presentationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<PresentationUpdateResponse> createSlide(@PathParam("presentationId") String presentationId) {
        return service.createSlidePage(presentationId);
    }

    @PUT
    @Path("update/{presentationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<PresentationUpdateResponse> updateSlides(@PathParam("presentationId") String presentationId, UpdateSlidePositions request) {
        return service.updateSlidesPosition(presentationId, request.getSlides());
    }
}
