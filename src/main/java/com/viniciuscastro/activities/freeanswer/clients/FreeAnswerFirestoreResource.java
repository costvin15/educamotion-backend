package com.viniciuscastro.activities.freeanswer.clients;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.viniciuscastro.activities.freeanswer.clients.requests.StoreFreeAnswerRequest;
import com.viniciuscastro.activities.freeanswer.models.FreeAnswer;
import com.viniciuscastro.exceptions.ApplicationException;
import com.viniciuscastro.exceptions.ApplicationException.StatusCode;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("firestore")
public class FreeAnswerFirestoreResource {
    @Inject
    Firestore firestore;

    private static final String FREE_ANSWER_COLLETION = "free_answers";

    @GET
    @Path("findFreeAnswerByActivityId/{activityId}")
    @Produces(MediaType.APPLICATION_JSON)
    public FreeAnswer findFreeAnswerByActivityId(@PathParam("activityid") String activityId) {
        CollectionReference freeAnswerCollection = firestore.collection(FREE_ANSWER_COLLETION);
        try {
            Log.info(activityId);
            Optional<QueryDocumentSnapshot> activity = freeAnswerCollection.whereEqualTo("id", activityId)
                .get().get()
                .getDocuments()
                .stream()
                .findFirst();

            if (!activity.isPresent()) {
                throw new ApplicationException("Pergunta de resposta livre não encontrada.", StatusCode.NOT_FOUND);
            }

            return new FreeAnswer(
                activity.get().getString("id"),
                activity.get().getString("presentationId"),
                activity.get().getString("question"),
                activity.get().getLong("maxWords"),
                activity.get().getTimestamp("createdAt"),
                activity.get().getTimestamp("updatedAt")
            );
        } catch (InterruptedException | ExecutionException | RuntimeException e) {
            Log.info(e.getMessage());
            throw new ApplicationException("Erro ao buscar pergunta de resposta livre.", StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("storeFreeAnswer")
    @Produces(MediaType.APPLICATION_JSON)
    public FreeAnswer storeFreeAnswer(StoreFreeAnswerRequest request) {
        CollectionReference freeAnswerCollection = firestore.collection(FREE_ANSWER_COLLETION);
        FreeAnswer freeAnswer = new FreeAnswer(request.getPresentationId(), request.getQuestion(), request.getMaxWords().longValue());

        freeAnswerCollection.add(freeAnswer);
        return freeAnswer;
    }
}
