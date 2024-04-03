package com.viniciuscastro.activity.clients;

import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.viniciuscastro.activity.clients.requests.StoreActivityRequest;
import com.viniciuscastro.activity.models.Activity;
import com.viniciuscastro.exceptions.ApplicationException;
import com.viniciuscastro.exceptions.ApplicationException.StatusCode;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("firestore")
public class ActivityFirestoreResource {
    @Inject
    Firestore firestore;

    private static final String ACTIVITY_COLLECTION = "activities";

    @GET
    @Path("getActivitiesByPresentationId/{presentationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Stream<Activity> getActivitiesByPresentationId(String presentationId) {
        CollectionReference activities = firestore.collection(ACTIVITY_COLLECTION);
        try {
            return activities.whereEqualTo("presentationId", presentationId)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(document -> new Activity(
                    document.getString("id"),
                    document.getString("presentationId"),
                    document.getString("activityId"),
                    document.getString("activityType"),
                    document.getTimestamp("createdAt"),
                    document.getTimestamp("updatedAt")
                ))
                .collect(Collectors.toList())
                .stream();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException("Erro ao buscar atividades.", StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

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
