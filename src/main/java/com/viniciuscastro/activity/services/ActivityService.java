package com.viniciuscastro.activity.services;

import com.viniciuscastro.activity.clients.ActivityFirestoreResource;
import com.viniciuscastro.activity.clients.requests.StoreActivityRequest;
import com.viniciuscastro.activity.dto.responses.ActivityListResponse;
import com.viniciuscastro.activity.dto.responses.ActivityResponse;
import com.viniciuscastro.activity.enums.ActivityType;
import com.viniciuscastro.activity.models.Activity;
import com.viniciuscastro.exceptions.ApplicationException;
import com.viniciuscastro.presentation.dto.response.PresentationWithSlidesResponse;
import com.viniciuscastro.presentation.services.PresentationService;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class ActivityService {
    @Inject
    ActivityFirestoreResource activityFirestoreResource;

    @Inject
    PresentationService presentationService;

    public Uni<ActivityListResponse> getActivitiesByPresentationId(String presentationId) {
        return Multi.createFrom().items(this.activityFirestoreResource.getActivitiesByPresentationId(presentationId))
            .onItem().transformToUniAndConcatenate(activity -> Uni.createFrom().item(() -> new ActivityResponse(
                activity.getId(),
                activity.getPresentationId(),
                activity.getActivityId(),
                activity.getActivityType().name(),
                activity.getCreatedAt().toDate(),
                activity.getUpdatedAt().toDate()
            )))
            .collect().asList()
            .onItem().transformToUni(list -> Uni.createFrom().item(new ActivityListResponse(list.size(), list)));
    }

    public Uni<ActivityResponse> validateAndStoreActivity(String presentationId, String activityId, String activityType) {
        return Uni.createFrom().item(() -> this.validateActivityType(activityType))
            .onItem().transformToUni(type -> this.validatePresentation(presentationId))
            .onItem().transformToUni(presentation -> this.presentationService.createSlidePage(presentationId))
            .onItem().transformToUni(presentation -> this.storeActivity(presentation.getPresentationId(), activityId, activityType));
    }

    private Uni<ActivityResponse> storeActivity(String presentationId, String activityId, String activityType) {
        return Uni.createFrom().item(() -> {
                StoreActivityRequest storeActivityRequest = new StoreActivityRequest(presentationId, activityId, ActivityType.valueOf(activityType));
                Activity activity = activityFirestoreResource.storeActivity(storeActivityRequest);
                return new ActivityResponse(
                    activity.getId(),
                    activity.getPresentationId(),
                    activity.getActivityId(),
                    activity.getActivityType().name(),
                    activity.getCreatedAt().toDate(),
                    activity.getUpdatedAt().toDate()
                );
            });
    }

    private Uni<PresentationWithSlidesResponse> validatePresentation(String presentationId) {
        return presentationService.getPresentationById(presentationId);
    }

    private ActivityType validateActivityType(String activityType) {
        try {
            return ActivityType.valueOf(activityType);
        } catch (IllegalArgumentException e) {
            throw new ApplicationException("Tipo de atividade inválido.", ApplicationException.StatusCode.BAD_REQUEST);
        }
    }
}
