package com.viniciuscastro.activities.freeanswer.clients;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.viniciuscastro.activities.freeanswer.clients.requests.StoreFreeAnswerRequest;
import com.viniciuscastro.activities.freeanswer.models.FreeAnswer;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("firestore")
public class FreeAnswerFirestoreResource {
    @Inject
    Firestore firestore;

    private static final String FREE_ANSWER_COLLETION = "free_answers";

    @POST
    @Path("storeFreeAnswer")
    @Produces(MediaType.APPLICATION_JSON)
    public FreeAnswer storeFreeAnswer(StoreFreeAnswerRequest request) {
        CollectionReference freeAnswerCollection = firestore.collection(FREE_ANSWER_COLLETION);
        FreeAnswer freeAnswer = new FreeAnswer(request.getPresentationId(), request.getQuestion(), request.getMaxWords());

        freeAnswerCollection.add(freeAnswer);
        return freeAnswer;
    }
}
