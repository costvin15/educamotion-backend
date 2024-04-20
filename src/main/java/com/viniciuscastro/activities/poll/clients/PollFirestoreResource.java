package com.viniciuscastro.activities.poll.clients;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.viniciuscastro.activities.poll.clients.requests.StoreChoiceRequest;
import com.viniciuscastro.activities.poll.clients.requests.StorePollRequest;
import com.viniciuscastro.activities.poll.models.Choice;
import com.viniciuscastro.activities.poll.models.Poll;
import com.viniciuscastro.activities.poll.models.PollAndChoices;
import com.viniciuscastro.exceptions.ApplicationException;
import com.viniciuscastro.exceptions.ApplicationException.StatusCode;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("firestore")
public class PollFirestoreResource {
    @Inject
    Firestore firestore;

    private static final String POLL_COLLECTION = "polls";
    private static final String CHOICE_COLLECTION = "choices";

    @GET
    @Path("findPollByActivityId/{activityId}")
    @Produces(MediaType.APPLICATION_JSON)
    public PollAndChoices findPollByActivityId(@PathParam("activityId") String activityId) {
        CollectionReference pollsCollection = firestore.collection(POLL_COLLECTION);
        CollectionReference choicesCollection= firestore.collection(CHOICE_COLLECTION);

        try {
            Optional<QueryDocumentSnapshot> poll = pollsCollection.whereEqualTo("id", activityId)
            .get()
            .get()
            .getDocuments()
            .stream()
            .findFirst();

            if (!poll.isPresent()) {
                throw new ApplicationException("Enquete não encontrada.", StatusCode.NOT_FOUND);
            }

            List<Choice> choices = choicesCollection.whereEqualTo("pollId", poll.get().getString("id"))
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(document -> new Choice(
                    document.getString("id"),
                    document.getString("pollId"),
                    document.getString("description"),
                    document.getTimestamp("createdAt"),
                    document.getTimestamp("updatedAt")
                ))
                .collect(Collectors.toList());

            return new PollAndChoices(
                poll.get().getString("id"),
                poll.get().getString("question"),
                poll.get().getString("presentationId"),
                poll.get().getTimestamp("createdAt"),
                poll.get().getTimestamp("updatedAt"),
                choices
            );
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException("Erro ao buscar enquete.", StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("storePoll")
    @Produces(MediaType.APPLICATION_JSON)
    public Poll storePoll(StorePollRequest request) {
        CollectionReference polls = firestore.collection(POLL_COLLECTION);
        Poll poll = new Poll(request.getPresentationId(), request.getQuestion());
        polls.add(poll);
        return poll;
    }

    @POST
    @Path("storeChoice")
    @Produces(MediaType.APPLICATION_JSON)
    public Choice storeChoice(StoreChoiceRequest request) {
        CollectionReference choices = firestore.collection(CHOICE_COLLECTION);
        Choice choice = new Choice(request.getDescription(), request.getPollId());
        choices.add(choice);
        return choice;
    }
}
