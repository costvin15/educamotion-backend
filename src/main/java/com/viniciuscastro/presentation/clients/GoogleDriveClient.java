package com.viniciuscastro.presentation.clients;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.viniciuscastro.presentation.models.Drive;
import com.viniciuscastro.presentation.models.DrivePage;

import io.quarkus.oidc.token.propagation.AccessToken;
import io.smallrye.mutiny.Uni;
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
    Uni<DrivePage> findFiles(@BeanParam Drive drive);
}
