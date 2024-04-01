package com.viniciuscastro.poll.clients;

import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.viniciuscastro.clients.PresentationFirestoreResource;
import com.viniciuscastro.exceptions.ApplicationException;
import com.viniciuscastro.exceptions.ApplicationException.StatusCode;
import com.viniciuscastro.poll.clients.requests.ChoiceRequest;
import com.viniciuscastro.poll.clients.requests.PollRequest;
import com.viniciuscastro.poll.models.Choice;
import com.viniciuscastro.poll.models.Poll;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("firestore")
public class PollFirestoreResource {
    @Inject
    Firestore firestore;
    @Inject
    PresentationFirestoreResource presentationFirestoreResource;

    private static final String POLL_COLLECTION = "polls";
    private static final String CHOICE_COLLECTION = "choices";

    @POST
    @Path("storePoll")
    @Produces(MediaType.APPLICATION_JSON)
    public Poll storePoll(PollRequest request) {
        Poll poll = new Poll(request.getQuestion(), request.getPresentationId());
        CollectionReference polls = firestore.collection(POLL_COLLECTION);

        try {
            polls.add(poll).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException("Erro ao inserir enquete.", StatusCode.INTERNAL_SERVER_ERROR);
        }
        return poll;
    }

    @POST
    @Path("storeChoice")
    @Produces(MediaType.APPLICATION_JSON)
    public Choice storeChoice(ChoiceRequest request) {
        Choice choice = new Choice(request.getDescription(), request.getPollId());
        CollectionReference choices = firestore.collection(CHOICE_COLLECTION);

        try {
            choices.add(choice).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException("Erro ao inserir opção de enquete.", StatusCode.INTERNAL_SERVER_ERROR);
        }
        return choice;
    }
}
