package com.viniciuscastro.session.clients;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.viniciuscastro.exceptions.ApplicationException;
import com.viniciuscastro.exceptions.ApplicationException.StatusCode;
import com.viniciuscastro.session.dto.requests.CreateSessionRequest;
import com.viniciuscastro.session.dto.requests.FinishSessionRequest;
import com.viniciuscastro.session.models.Session;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("firestore")
public class SessionFirestoreResource {
    @Inject
    Firestore firestore;

    private static final String SESSION_COLLECTION = "sessions";

    @POST
    @Path("createSession")
    @Produces(MediaType.APPLICATION_JSON)
    public Session createSession(CreateSessionRequest request) {
        Optional<Session> activeSession = findActiveSessionByPresentationId(request.getPresentationId());
        if (activeSession.isPresent()) {
            return activeSession.get();
        }

        CollectionReference sessionsCollection = firestore.collection(SESSION_COLLECTION);
        Session session = new Session(request.getPresentationId());

        ApiFuture<DocumentReference> transaction = sessionsCollection.add(session);
        try {
            session.setId(transaction.get().getId());
            sessionsCollection.document(session.getId()).set(session);
        } catch (InterruptedException | ExecutionException exception) {
        }

        return session;
    }

    @DELETE
    @Path("finishSession")
    @Produces(MediaType.APPLICATION_JSON)
    public Session finishSession(FinishSessionRequest request) {
        Optional<Session> activeSession = findActiveSessionByPresentationId(request.getPresentationId());
        if (activeSession.isEmpty()) {
            throw new ApplicationException("Sessão não encontrada.", StatusCode.NOT_FOUND);
        }

        CollectionReference sessionsCollection = firestore.collection(SESSION_COLLECTION);
        Session session = activeSession.get();
        session.setActive(false);
        sessionsCollection.document(session.getId()).set(session);
        return session;
    }

    private Optional<Session> findActiveSessionByPresentationId(String presentationId) {
        CollectionReference sessionsCollection = firestore.collection(SESSION_COLLECTION);

        try {
            return sessionsCollection.whereEqualTo("presentationId", presentationId)
                .whereEqualTo("active", true)
                .get()
                .get()
                .getDocuments()
                .stream()
                .findFirst()
                .map(document -> Optional.of(new Session(
                    document.getId(),
                    document.getString("code"),
                    document.getString("presentationId"),
                    document.getBoolean("active"),
                    document.getTimestamp("createdAt"),
                    document.getTimestamp("updatedAt")
                )))
                .orElse(Optional.empty());
        } catch (InterruptedException | ExecutionException exception) {
            return Optional.empty();
        }
    }
}
