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
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/firestore")
public class GoogleFirestoreResource {
    @Inject
    Firestore firestore;

    @Inject
    UserInfo userInfo;

    private static final String PRESENTATION_COLLECTION = "presentations";
    private static final String THUMBNAIL_COLLECTION = "thumbnails";

    @GET
    @Path("storePresentation/{presentationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String storePresentation(@PathParam("presentationId") String presentationId) {
        CollectionReference presentations = firestore.collection(PRESENTATION_COLLECTION);
        try {
            presentations.add(new StorePresentationRequest(presentationId, userInfo.getSubject()))
                .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException("Erro ao importar apresentação.", StatusCode.INTERNAL_SERVER_ERROR);
        }
        return presentationId;
    }

    @POST
    @Path("storeThumbnail")
    @Produces(MediaType.APPLICATION_JSON)
    public BucketFile storeThumbnail(BucketFile file) {
        CollectionReference presentations = firestore.collection(THUMBNAIL_COLLECTION);
        try {
            presentations.add(new StoreThumbnailRequest(file.getThumbnail().getPresentationId(), file.getThumbnail().getPageObjectId()))
                .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException("Erro ao importar imagem.", StatusCode.INTERNAL_SERVER_ERROR);
        }
        return file;
    }

    @GET
    @Path("searchPresentation/{presentationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public GooglePresentationSearchResult searchPresentation(@PathParam("presentationId") String presentationId) {
        CollectionReference presentations = firestore.collection(PRESENTATION_COLLECTION);
        try {
            return presentations.whereEqualTo("presentationId", presentationId)
                .whereEqualTo("userId", userInfo.getSubject())
                .get()
                .get()
                .getDocuments()
                .stream()
                .findFirst()
                .map(document -> new GooglePresentationSearchResult(
                    document.getString("presentationId"),
                    document.getTimestamp("createdAt"),
                    document.getTimestamp("updatedAt"),
                    true
                ))
                .orElse(new GooglePresentationSearchResult(presentationId));
        } catch (InterruptedException | ExecutionException e) {
            return new GooglePresentationSearchResult(presentationId);
        }
    }

    @GET
    @Path("searchAllImportedPresentations")
    @Produces(MediaType.APPLICATION_JSON)
    public Stream<GooglePresentationSearchResult> searchAllImportedPresentations() {
        CollectionReference presentations = firestore.collection(PRESENTATION_COLLECTION);
        try {
            return presentations.whereEqualTo("userId", userInfo.getSubject())
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(document -> new GooglePresentationSearchResult(
                    document.getString("presentationId"),
                    document.getTimestamp("createdAt"),
                    document.getTimestamp("updatedAt"),
                    true
                ))
                .collect(Collectors.toList())
                .stream();
        } catch (InterruptedException | ExecutionException e) {
            throw new ApplicationException("Erro ao buscar apresentações.", StatusCode.INTERNAL_SERVER_ERROR);
        }
    }
}