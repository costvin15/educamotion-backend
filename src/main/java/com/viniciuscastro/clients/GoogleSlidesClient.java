package com.viniciuscastro.clients;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.viniciuscastro.clients.models.GooglePresentation;
import com.viniciuscastro.clients.models.GoogleThumbnail;
import com.viniciuscastro.clients.models.requests.PresentationUpdateRequest;
import com.viniciuscastro.clients.models.responses.PresentationBatchUpdateResponse;

import io.quarkus.oidc.token.propagation.AccessToken;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@AccessToken
@RegisterRestClient
@Path("/")
public interface GoogleSlidesClient {
    @GET
    @Path("/{presentationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<GooglePresentation> getPresentation(@PathParam("presentationId") String presentationId);

    @GET
    @Path("/{presentationId}/pages/{pageObjectId}/thumbnail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Retry(maxRetries = 100, delay = 10000, maxDuration = 100000, jitter = 10000, retryOn = { Exception.class } )
    Uni<GoogleThumbnail> getPresentationThumbnail(@PathParam("presentationId") String presentationId, @PathParam("pageObjectId") String pageObjectId);

    @POST
    @Path("/{presentationId}:batchUpdate")
    Uni<PresentationBatchUpdateResponse> performBatchUpdate(@PathParam("presentationId") String presentationId, PresentationUpdateRequest presentationUpdate);
}
