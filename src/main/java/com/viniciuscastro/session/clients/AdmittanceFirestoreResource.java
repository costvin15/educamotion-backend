package com.viniciuscastro.session.clients;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.viniciuscastro.exceptions.ApplicationException;
import com.viniciuscastro.exceptions.ApplicationException.StatusCode;
import com.viniciuscastro.session.models.Admittance;
import com.viniciuscastro.session.models.Session;

import io.quarkus.oidc.UserInfo;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

@Path("/firestore")
public class AdmittanceFirestoreResource {
    @Inject
    Firestore firestore;

    @Inject
    UserInfo userInfo;

    private static final String ADMITTANCE_COLLECTION = "admittances";
    private static final String SESSION_COLLECTION = "sessions";

    public Admittance registerAdmittance(String sessionCode) {
        Optional<Session> session = findActiveSessionBySessionCode(sessionCode);
        if (session.isEmpty()) {
            throw new ApplicationException("Sessão não encontrada.", StatusCode.NOT_FOUND);
        }

        Optional<Admittance> existentAdmittance = findAdmittanceByUserIdAndSessionId(userInfo.getSubject(), session.get().getId());
        if (existentAdmittance.isPresent()) {
            return existentAdmittance.get();
        }

        CollectionReference admittancesCollection = firestore.collection(ADMITTANCE_COLLECTION);
        String userId = userInfo.getSubject();
        Admittance admittance = new Admittance(userId, session.get().getId());
        admittancesCollection.add(admittance);
        return admittance;
    }

    private Optional<Admittance> findAdmittanceByUserIdAndSessionId(String userId, String sessionId) {
        CollectionReference admittancesCollection = firestore.collection(ADMITTANCE_COLLECTION);

        try {
            return admittancesCollection.whereEqualTo("userId", userId)
                .whereEqualTo("sessionId", sessionId)
                .get()
                .get()
                .getDocuments()
                .stream()
                .findFirst()
                .map(document -> Optional.of(new Admittance(
                    document.getString("id"),
                    document.getString("userId"),
                    document.getString("sessionId"),
                    document.getTimestamp("createdAt")
                )))
                .orElse(Optional.empty());
        } catch (InterruptedException | ExecutionException e) {
            return Optional.empty();
        }
    }

    private Optional<Session> findActiveSessionBySessionCode(String sessionCode) {
        CollectionReference sessionsCollection = firestore.collection(SESSION_COLLECTION);

        try {
            return sessionsCollection.whereEqualTo("code", sessionCode)
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
        } catch (InterruptedException | ExecutionException e) {
            return Optional.empty();
        }
    }
}
