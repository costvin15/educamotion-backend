package com.viniciuscastro.presentation.services;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.stream.Collectors;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.cloud.storage.BlobId;
import com.viniciuscastro.clients.GoogleCloudStorageResource;
import com.viniciuscastro.clients.GoogleDriveClient;
import com.viniciuscastro.clients.GoogleSlidesClient;
import com.viniciuscastro.clients.models.Presentation;
import com.viniciuscastro.clients.models.PresentationThumbnail;
import com.viniciuscastro.clients.models.PresentationUpdate;
import com.viniciuscastro.clients.models.PresentationUpdateResponse;
import com.viniciuscastro.clients.models.WriteControl;
import com.viniciuscastro.clients.models.requests.CreateSlideRequest;
import com.viniciuscastro.clients.models.requests.LayoutReference;
import com.viniciuscastro.clients.models.requests.Request;
import com.viniciuscastro.clients.models.requests.LayoutReference.PredefinedLayout;
import com.viniciuscastro.presentation.models.BucketFile;
import com.viniciuscastro.presentation.models.Drive;
import com.viniciuscastro.presentation.models.DrivePage;
import com.viniciuscastro.presentation.models.Thumbnail;
import com.viniciuscastro.presentation.resources.MimeType;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class PresentationService {
    @Inject
    @RestClient
    GoogleDriveClient driveClient;
    
    @Inject
    @RestClient
    GoogleSlidesClient slidesClient;

    @Inject
    GoogleCloudStorageResource googleCloudStorageResource;

    @Inject
    PgPool client;

    Logger logger = LoggerFactory.getLogger(PresentationService.class);

    public Uni<Long> importPresentations(String[] presentationIds) {
        return Multi.createFrom().items(presentationIds)
            .onItem().transformToMultiAndMerge(presentationId -> this.getAllThumbnails(presentationId))
            .onItem().transformToUniAndConcatenate(thumbnail -> this.getThumbnailBytes(thumbnail))
            .onItem().transformToUniAndConcatenate(file -> this.storeThumbnailOnStorage(file))
            .collect().with(Collectors.counting());
    }

    public Uni<DrivePage> findPresentationsFromDrive(String pageToken) {
        return this.driveClient.findFiles(new Drive(MimeType.PRESENTATION, pageToken, 30));
    }

    public Uni<Presentation> findPresentationInformation(String presentationId) {
        return this.slidesClient.getPresentation(presentationId);
    }

    private Uni<BucketFile> getThumbnailBytes(Thumbnail thumbnail) {
        return Uni.createFrom().item(() -> {
            try {
                BufferedInputStream input = new BufferedInputStream(new URL(thumbnail.getContentUrl()).openStream());
                byte[] content = input.readAllBytes();
                return new BucketFile(thumbnail.getPresentationId() + "/" + thumbnail.getPageObjectId(), "image/png", content);
            } catch (IOException e) {
                // Melhor lançar uma excenção e trata-la em um middleware
                return null;
            }
        });
    }

    private Uni<BlobId> storeThumbnailOnStorage(BucketFile file) {
        return Uni.createFrom().item(() -> this.googleCloudStorageResource.storage(file));
    }

    private Multi<Thumbnail> getAllThumbnails(String presentationId) {
        Uni<Presentation> slideInformation = this.findPresentationInformation(presentationId);
        return slideInformation.onItem().transformToMulti(presentation -> {
            if (presentation.getSlides() == null || presentation.getSlides().isEmpty()) {
                return Multi.createFrom().empty();
            }
            
            return Multi.createFrom().iterable(presentation.getSlides())
                .onItem()
                .transformToUni(slideItem -> {
                    String objectId = slideItem.getObjectId();
                    Uni<PresentationThumbnail> presentationThumbnail = this.slidesClient.getPresentationThumbnail(presentationId, slideItem.getObjectId());
                    return presentationThumbnail.onItem().transformToUni(thumbnail -> {
                        return Uni.createFrom().item(() -> new Thumbnail(presentationId, objectId, thumbnail.getContentUrl()));
                    });
                })
                .merge();
        });
    }

    /**
     * TODO: Há um caso de uso que não está sendo atendido, que é quando o usuário não tem permissão para
     * editar esse slide (Por exemplo: O slide foi compartilhado com ele apenas para visualização)
     */
    public Uni<PresentationUpdateResponse> createSlidePage(String presentationId) {
        Uni<Presentation> presentationInformation = this.findPresentationInformation(presentationId);
        return presentationInformation.onItem().transformToUni(presentation -> {
            LayoutReference layoutReference = new LayoutReference(PredefinedLayout.TITLE_AND_TWO_COLUMNS, null);
            CreateSlideRequest createSlideRequest = new CreateSlideRequest(null, 0, layoutReference, null);
    
            Request request = new Request(createSlideRequest);
            Request[] requests = { request };
            WriteControl writeControl = new WriteControl(presentation.getRevisionId());
    
            PresentationUpdate presentationUpdate = new PresentationUpdate(requests, writeControl);
            return this.slidesClient.performBatchUpdate(presentationId, presentationUpdate);
        });
    }
}
