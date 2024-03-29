package com.viniciuscastro.presentation.controllers;

import com.viniciuscastro.clients.models.GooglePresentation;
import com.viniciuscastro.clients.models.responses.PresentationUpdateResponse;
import com.viniciuscastro.presentation.dto.request.ImportPresentation;
import com.viniciuscastro.presentation.models.BucketFile;
import com.viniciuscastro.presentation.models.DrivePage;
import com.viniciuscastro.presentation.services.PresentationService;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Authenticated
@Path("presentation")
public class PresentationController {
    @Inject
    PresentationService slidesService;

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<DrivePage> findPage(@QueryParam("pageToken") String pageToken) {
        return this.slidesService.findPresentationsFromDrive(pageToken);
    }

    @POST
    @Path("import")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<BucketFile> importPresentations(ImportPresentation request) {
        return this.slidesService.importPresentations(request.getPresentationIds());
    }

    @GET
    @Path("{presentationId}")
    public Uni<GooglePresentation> findPresentationInformation(@PathParam("presentationId") String presentationId) {
        return this.slidesService.findPresentationInformation(presentationId);
    }

    @GET
    @Path("imported")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<GooglePresentation> findImportedPresentations() {
        return this.slidesService.searchAllImportedPresentations();
    }

    @GET
    @Path("create/{presentationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<PresentationUpdateResponse> createSlide(@PathParam("presentationId") String presentationId) {
        return this.slidesService.createSlidePage(presentationId);
    }
}
