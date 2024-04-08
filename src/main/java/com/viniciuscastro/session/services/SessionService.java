package com.viniciuscastro.session.services;

import com.viniciuscastro.session.clients.SessionFirestoreResource;
import com.viniciuscastro.session.dto.requests.CreateSessionRequest;
import com.viniciuscastro.session.dto.requests.FinishSessionRequest;
import com.viniciuscastro.session.dto.responses.SessionResponse;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class SessionService {
    @Inject
    SessionFirestoreResource sessionFirestoreResource;

    public Uni<SessionResponse> createSession(CreateSessionRequest request) {
        return Uni.createFrom().item(this.sessionFirestoreResource.createSession(request))
            .onItem().transformToUni(session -> {
                return Uni.createFrom().item(new SessionResponse(
                    session.getCode(),
                    session.getPresentationId(),
                    session.getActive(),
                    session.getCreatedAt().toDate(),
                    session.getUpdatedAt().toDate()
                ));
            });
    }

    public Uni<SessionResponse> finishSession(FinishSessionRequest request) {
        return Uni.createFrom().item(this.sessionFirestoreResource.finishSession(request))
            .onItem().transformToUni(session -> {
                return Uni.createFrom().item(new SessionResponse(
                    session.getCode(),
                    session.getPresentationId(),
                    session.getActive(),
                    session.getCreatedAt().toDate(),
                    session.getUpdatedAt().toDate()
                ));
            });
    }
}
