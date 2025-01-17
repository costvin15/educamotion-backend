package com.viniciuscastro.controllers;

import java.util.List;

import com.viniciuscastro.dto.response.AvailablePresentationListResponse;
import com.viniciuscastro.dto.response.PresentationDetailResponse;
import com.viniciuscastro.dto.response.PresentationListResponse;
import com.viniciuscastro.dto.response.PresentationResponse;
import com.viniciuscastro.dto.response.openai.InteractiveObjects;
import com.viniciuscastro.services.ElementService;
import com.viniciuscastro.services.PresentationService;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

@Authenticated
@Path("presentation")
public class PresentationController {
    @Inject
    PresentationService presentationService;

    @Inject
    ElementService elementService;

    @POST
    @Path("add/{presentationId}")
    public PresentationDetailResponse createPresentation(String presentationId) {
        return this.presentationService.addPresentation(presentationId);
    }

    @GET
    @Path("list")
    public PresentationListResponse getPresentations() {
        List<PresentationResponse> presentations = this.presentationService.getPresentations();
        return new PresentationListResponse(presentations);
    }

    @GET
    @Path("available")
    public AvailablePresentationListResponse getAvailablePresentations(@QueryParam(value = "search") String searchQuery, @QueryParam(value = "nextPageToken") String nextPageToken) {
        if (nextPageToken == null) {
            nextPageToken = "";
        }
        return this.presentationService.getAvailablePresentations(searchQuery, nextPageToken);
    }

    @GET
    @Path("detail/{presentationId}")
    public PresentationDetailResponse getPresentationDetails(String presentationId) {
        return this.presentationService.getPresentationDetails(presentationId);
    }

    @GET
    @Path("thumbnail/{presentationId}/{slideId}")
    @Produces("image/png")
    public byte[] fetchImage(String presentationId, String slideId) {
        return this.presentationService.getStoredThumbnail(presentationId, slideId);
    }

    @GET
    @Path("generate-elements/{presentationId}/{slideId}")
    public InteractiveObjects generateElements(String presentationId, String slideId) {
        return this.presentationService.elaborateSlide(presentationId, slideId);
    }
}
