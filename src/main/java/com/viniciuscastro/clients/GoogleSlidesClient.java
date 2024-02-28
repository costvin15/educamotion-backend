package com.viniciuscastro.clients;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.viniciuscastro.clients.models.Presentation;
import com.viniciuscastro.clients.models.PresentationThumbnail;

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
    Uni<Presentation> getPresentation(@PathParam("presentationId") String presentationId);

    @GET
    @Path("/{presentationId}/pages/{pageObjectId}/thumbnail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<PresentationThumbnail> getPresentationThumbnail(@PathParam("presentationId") String presentationId, @PathParam("pageObjectId") String pageObjectId);

    @POST
    @Path("/{presentationId}:batchUpdate")
    Uni<String> performBatchUpdate();
}
