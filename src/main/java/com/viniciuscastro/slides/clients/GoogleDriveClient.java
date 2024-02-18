package com.viniciuscastro.slides.clients;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.viniciuscastro.slides.models.Drive;
import com.viniciuscastro.slides.models.DrivePage;

import io.quarkus.oidc.token.propagation.AccessToken;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@AccessToken
@RegisterRestClient
@Path("/")
public interface GoogleDriveClient {
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    DrivePage findFiles(@BeanParam Drive drive);
}
