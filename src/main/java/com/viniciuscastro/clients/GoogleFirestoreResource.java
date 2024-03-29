package com.viniciuscastro.clients;

import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.viniciuscastro.clients.models.GooglePresentationSearchResult;
import com.viniciuscastro.clients.models.requests.StorePresentationRequest;
import com.viniciuscastro.clients.models.requests.StoreThumbnailRequest;
import com.viniciuscastro.exceptions.ApplicationException;
import com.viniciuscastro.exceptions.ApplicationException.StatusCode;
import com.viniciuscastro.presentation.models.BucketFile;

import io.quarkus.oidc.UserInfo;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/firestore")
public class GoogleFirestoreResource {
    @Inject
    Firestore firestore;

    @Inject
    UserInfo userInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String storePresentation(String presentationId) {
        CollectionReference presentations = firestore.collection("presentations");
        try {
            presentations.add(new StorePresentationRequest(presentationId, userInfo.getSubject()))
                .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException("Erro ao importar apresentação.", StatusCode.INTERNAL_SERVER_ERROR);
        }
        return presentationId;
    }

    public BucketFile storeThumbnail(BucketFile file) {
        CollectionReference presentations = firestore.collection("thumbnails");
        try {
            presentations.add(new StoreThumbnailRequest(file.getThumbnail().getPresentationId(), file.getThumbnail().getPageObjectId()))
                .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException("Erro ao importar imagem.", StatusCode.INTERNAL_SERVER_ERROR);
        }
        return file;
    }

    public GooglePresentationSearchResult searchPresentation(String presentationId) {
        CollectionReference presentations = firestore.collection("presentations");
        try {
            return presentations.whereEqualTo("presentationId", presentationId)
                .whereEqualTo("userId", userInfo.getSubject())
                .get()
                .get()
                .getDocuments()
                .stream()
                .findFirst()
                .map(document -> new GooglePresentationSearchResult(document.getString("presentationId"), true))
                .orElse(new GooglePresentationSearchResult(presentationId, false));
        } catch (InterruptedException | ExecutionException e) {
            return new GooglePresentationSearchResult(presentationId, false);
        }
    }

    public Stream<GooglePresentationSearchResult> searchAllImportedPresentations() {
        CollectionReference presentations = firestore.collection("presentations");
        try {
            return presentations.whereEqualTo("userId", userInfo.getSubject())
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(document -> new GooglePresentationSearchResult(document.getString("presentationId"), true))
                .collect(Collectors.toList())
                .stream();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException("Erro ao buscar apresentações.", StatusCode.INTERNAL_SERVER_ERROR);
        }
    }
}