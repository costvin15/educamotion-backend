package com.viniciuscastro.activity.clients;

import com.google.cloud.firestore.Firestore;
import com.viniciuscastro.activity.clients.requests.StoreActivityRequest;
import com.viniciuscastro.activity.models.Activity;
import com.viniciuscastro.exceptions.ApplicationException;
import com.viniciuscastro.exceptions.ApplicationException.StatusCode;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("firestore")
public class ActivityFirestoreResource {
    @Inject
    Firestore firestore;

    private static final String ACTIVITY_COLLECTION = "activities";

    @POST
    @Path("storeActivity")
    @Produces(MediaType.APPLICATION_JSON)
    public Activity storeActivity(StoreActivityRequest request) {
        Activity activity = new Activity(
            request.getPresentationId(),
            request.getActivityId(),
            request.getActivityType()
        );
        try {
            firestore.collection(ACTIVITY_COLLECTION).add(activity).get();
        } catch (Exception e) {
            throw new ApplicationException("Erro ao inserir atividade.", StatusCode.INTERNAL_SERVER_ERROR);
        }
        return activity;
    }
}
