package com.viniciuscastro.activities.freeanswer.services;

import com.viniciuscastro.activities.freeanswer.clients.FreeAnswerFirestoreResource;
import com.viniciuscastro.activities.freeanswer.clients.requests.StoreFreeAnswerRequest;
import com.viniciuscastro.activities.freeanswer.dto.responses.StoreFreeAnswerResponse;
import com.viniciuscastro.activity.dto.responses.ActivityResponse;
import com.viniciuscastro.activity.services.ActivityService;
import com.viniciuscastro.presentation.dto.response.PresentationWithSlidesResponse;
import com.viniciuscastro.presentation.services.PresentationService;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class FreeAnswerService {
    @Inject
    FreeAnswerFirestoreResource freeAnswerFirestoreResource;

    @Inject
    PresentationService presentationService;

    @Inject
    ActivityService activityService;

    private static final String FREE_ANSWER_TYPE = "free_answer";

    public Uni<ActivityResponse> validateAndStoreFreeAnswer(String presentationId, String question, Integer maxWords) {
        return this.validatePresentation(presentationId)
            .onItem().transformToUni(presentation -> this.storeFreeAnswer(presentationId, question, maxWords))
            .onItem().transformToUni(freeAnswer -> this.storeActivity(freeAnswer.getPresentationId(), freeAnswer.getId()));
    }

    private Uni<StoreFreeAnswerResponse> storeFreeAnswer(String presentationId, String question, Integer maxWords) {
        return Uni.createFrom().item(new StoreFreeAnswerRequest(question, maxWords, presentationId))
            .onItem().transform(request -> this.freeAnswerFirestoreResource.storeFreeAnswer(request))
            .onItem().transform(freeAnswer -> {
                return new StoreFreeAnswerResponse(
                    freeAnswer.getId(),
                    freeAnswer.getPresentationId(),
                    freeAnswer.getQuestion(),
                    freeAnswer.getMaxWords(),
                    freeAnswer.getCreatedAt().toDate(),
                    freeAnswer.getUpdatedAt().toDate()
                );
            });
    }

    private Uni<ActivityResponse> storeActivity(String presentationId, String activityId) {
        return Uni.createFrom().item(presentationId)
            .onItem().transformToUni(presentation -> this.activityService.validateAndStoreActivity(presentationId, activityId, FREE_ANSWER_TYPE));
    }

    private Uni<PresentationWithSlidesResponse> validatePresentation(String presentationId) {
        return this.presentationService.getPresentationById(presentationId);
    }
}
