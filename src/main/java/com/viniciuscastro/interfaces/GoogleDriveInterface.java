package com.viniciuscastro.interfaces;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.viniciuscastro.dto.request.GoogleDriveSearchRequest;
import com.viniciuscastro.dto.response.GoogleDriveSearchResponse;

import io.quarkus.oidc.token.propagation.AccessToken;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@AccessToken
@RegisterRestClient
public interface GoogleDriveInterface {
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GoogleDriveSearchResponse findFiles(@BeanParam GoogleDriveSearchRequest request);
}
