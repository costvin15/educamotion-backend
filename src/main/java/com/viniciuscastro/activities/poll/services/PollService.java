package com.viniciuscastro.activities.poll.services;

import java.util.List;
import java.util.stream.Collectors;

import com.viniciuscastro.activities.poll.clients.PollFirestoreResource;
import com.viniciuscastro.activities.poll.clients.requests.StoreChoiceRequest;
import com.viniciuscastro.activities.poll.clients.requests.StorePollRequest;
import com.viniciuscastro.activities.poll.dto.responses.ChoiceResponse;
import com.viniciuscastro.activities.poll.dto.responses.PollResponse;
import com.viniciuscastro.activities.poll.dto.responses.StorePollResponse;
import com.viniciuscastro.activities.poll.models.Poll;
import com.viniciuscastro.activity.dto.responses.ActivityResponse;
import com.viniciuscastro.activity.services.ActivityService;
import com.viniciuscastro.presentation.dto.response.PresentationWithSlidesResponse;
import com.viniciuscastro.presentation.services.PresentationService;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class PollService {
    @Inject
    PollFirestoreResource pollFirestoreResource;

    @Inject
    PresentationService presentationService;

    @Inject
    ActivityService activityService;

    private static final String POLL_TYPE = "poll";

    public Uni<PollResponse> findPollByActivityId(String activityId) {
        return Uni.createFrom().item(this.pollFirestoreResource.findPollByActivityId(activityId))
            .onItem().transformToUni(poll -> {
                List<ChoiceResponse> choices = poll.getChoices().stream().map(choice -> {
                    return new ChoiceResponse(
                        choice.getId(),
                        choice.getPollId(),
                        choice.getDescription(),
                        choice.getCreatedAt().toDate(),
                        choice.getUpdatedAt().toDate()
                    );
                }).collect(Collectors.toList());

                PollResponse pollResponse = new PollResponse(
                    poll.getId(),
                    poll.getPresentationId(),
                    poll.getQuestion(),
                    choices,
                    poll.getCreatedAt().toDate(),
                    poll.getUpdatedAt().toDate()
                );

                return Uni.createFrom().item(pollResponse);
            });
    }

    public Uni<ActivityResponse> validateAndStorePoll(String presentationId, String question, String[] choices) {
        return Uni.createFrom().item(presentationId)
            .onItem().transformToUni(presentation -> this.validatePresentation(presentation))
            .onItem().transformToUni(presentation -> this.storePoll(presentation.getPresentationId(), question, choices))
            .onItem().transformToUni(poll -> this.storeActivity(presentationId, poll.getId()));
    }

    private Uni<StorePollResponse> storePoll(String presentationId, String question, String[] choices) {
        return Uni.createFrom().item(() -> {
            StorePollRequest pollRequest = new StorePollRequest(question, presentationId);
            Poll poll = pollFirestoreResource.storePoll(pollRequest);

            for (String choice : choices) {
                StoreChoiceRequest choiceRequest = new StoreChoiceRequest(choice, poll.getId());
                pollFirestoreResource.storeChoice(choiceRequest);
            }

            return new StorePollResponse(
                poll.getId(),
                poll.getPresentationId(),
                poll.getQuestion(),
                choices,
                poll.getCreatedAt().toDate(),
                poll.getUpdatedAt().toDate()
            );
        });
    }

    private Uni<ActivityResponse> storeActivity(String presentationId, String activityId) {
        return Uni.createFrom().item(presentationId)
            .onItem().transformToUni(presentation -> this.activityService.validateAndStoreActivity(presentation, activityId, POLL_TYPE));
    }

    private Uni<PresentationWithSlidesResponse> validatePresentation(String presentationId) {
        return presentationService.getPresentationById(presentationId);
    }
}
