package com.viniciuscastro.clients;

import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.viniciuscastro.clients.models.requests.PresentationFirestore;
import com.viniciuscastro.clients.models.requests.PresentationSearchResult;
import com.viniciuscastro.clients.models.requests.ThumbnailFirestore;
import com.viniciuscastro.presentation.models.BucketFile;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/firestore")
public class GoogleFirestoreResource {
    @Inject
    Firestore firestore;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String storePresentation(String presentationId) {
        CollectionReference presentations = firestore.collection("presentations");
        try {
            presentations.add(new PresentationFirestore(presentationId))
                .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return presentationId;
    }

    public BucketFile storeThumbnail(BucketFile file) {
        CollectionReference presentations = firestore.collection("thumbnails");
        try {
            presentations.add(new ThumbnailFirestore(file.getThumbnail().getPresentationId(), file.getThumbnail().getPageObjectId()))
                .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return file;
    }

    public PresentationSearchResult searchPresentation(String presentationId) {
        CollectionReference presentations = firestore.collection("presentations");
        try {
            return presentations.whereEqualTo("presentationId", presentationId)
                .get()
                .get()
                .getDocuments()
                .stream()
                .findFirst()
                .map(document -> new PresentationSearchResult(document.getString("presentationId"), true))
                .orElse(new PresentationSearchResult(presentationId, false));
        } catch (InterruptedException | ExecutionException e) {
            return new PresentationSearchResult(presentationId, false);
        }
    }
}