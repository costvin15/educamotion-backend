package com.viniciuscastro.poll.services;

import com.viniciuscastro.poll.clients.PollFirestoreResource;
import com.viniciuscastro.poll.clients.requests.ChoiceRequest;
import com.viniciuscastro.poll.clients.requests.PollRequest;
import com.viniciuscastro.poll.models.Poll;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class PollService {
    @Inject
    PollFirestoreResource pollFirestoreResource;

    public Poll storePoll() {
        PollRequest pollRequest = new PollRequest("Hello from the world?", "1234");
        Poll poll = pollFirestoreResource.storePoll(pollRequest);
        ChoiceRequest choiceRequest1 = new ChoiceRequest("Yes", poll.getId());
        ChoiceRequest choiceRequest2 = new ChoiceRequest("No", poll.getId());
        pollFirestoreResource.storeChoice(choiceRequest1);
        pollFirestoreResource.storeChoice(choiceRequest2);

        return poll;
    }
}
