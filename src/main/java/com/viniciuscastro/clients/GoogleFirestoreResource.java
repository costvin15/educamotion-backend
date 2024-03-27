package com.viniciuscastro.clients;

import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.viniciuscastro.clients.models.requests.PresentationFirestore;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/firestore")
public class GoogleFirestoreResource {
    @Inject
    Firestore firestore;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String firestore(String presentationId) {
        CollectionReference presentations = firestore.collection("presentations");
        try {
            presentations.add(new PresentationFirestore(presentationId))
                .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return presentationId;
    }
}
