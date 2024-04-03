package com.viniciuscastro.poll.services;

import com.viniciuscastro.poll.clients.PollFirestoreResource;
import com.viniciuscastro.poll.clients.requests.ChoiceRequest;
import com.viniciuscastro.poll.clients.requests.PollRequest;
import com.viniciuscastro.poll.dto.responses.PollResponse;
import com.viniciuscastro.poll.models.Poll;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class PollService {
    @Inject
    PollFirestoreResource pollFirestoreResource;

    public PollResponse storePoll(String presentationId, String question, String[] choices) {
        PollRequest pollRequest = new PollRequest(question, presentationId);
        Poll poll = pollFirestoreResource.storePoll(pollRequest);

        for (String choice : choices) {
            ChoiceRequest choiceRequest = new ChoiceRequest(choice, poll.getId());
            pollFirestoreResource.storeChoice(choiceRequest);
        }

        return new PollResponse(
            poll.getId(),
            poll.getPresentationId(),
            poll.getQuestion(),
            choices,
            poll.getCreatedAt().toDate(),
            poll.getUpdatedAt().toDate()
        );
    }
}
