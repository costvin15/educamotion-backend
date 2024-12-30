package com.viniciuscastro.interfaces;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.viniciuscastro.models.googleslide.GoogleSlide;
import com.viniciuscastro.models.googleslide.GoogleSlideImage;

import io.quarkus.oidc.token.propagation.AccessToken;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@AccessToken
@RegisterRestClient
public interface GoogleSlidesInterface {
    @GET
    @Path("{presentationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    GoogleSlide getPresentation(@PathParam("presentationId") String presentationId);

    @GET
    @Path("{presentationId}/pages/{pageId}/thumbnail")
    GoogleSlideImage getPresentationImage(@PathParam("presentationId") String presentationId, @PathParam("pageId") String pageId);
}
