package com.viniciuscastro.presentation.services;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.viniciuscastro.clients.GoogleCloudStorageResource;
import com.viniciuscastro.clients.GoogleDriveClient;
import com.viniciuscastro.clients.GoogleFirestoreResource;
import com.viniciuscastro.clients.GoogleSlidesClient;
import com.viniciuscastro.clients.models.Presentation;
import com.viniciuscastro.clients.models.PresentationThumbnail;
import com.viniciuscastro.clients.models.PresentationUpdate;
import com.viniciuscastro.clients.models.PresentationUpdateResponse;
import com.viniciuscastro.clients.models.WriteControl;
import com.viniciuscastro.clients.models.requests.CreateSlideRequest;
import com.viniciuscastro.clients.models.requests.LayoutReference;
import com.viniciuscastro.clients.models.requests.PresentationSearchResult;
import com.viniciuscastro.clients.models.requests.Request;
import com.viniciuscastro.clients.models.requests.LayoutReference.PredefinedLayout;
import com.viniciuscastro.presentation.models.BucketFile;
import com.viniciuscastro.presentation.models.Drive;
import com.viniciuscastro.presentation.models.DrivePage;
import com.viniciuscastro.presentation.models.Thumbnail;
import com.viniciuscastro.presentation.resources.MimeType;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
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
    GoogleFirestoreResource googleFirestoreResource;

    Logger logger = LoggerFactory.getLogger(PresentationService.class);

    public Multi<BucketFile> importPresentations(String[] presentationIds) {
        return Multi.createFrom().items(presentationIds)
            .onItem().transformToUniAndConcatenate(presentationId -> this.findPresentation(presentationId))
            .onItem().transformToMultiAndConcatenate(presentation -> {
                if (presentation.isExists()) {
                    logger.info("Presentation {} already exists", presentation.getPresentationId());
                    return Multi.createFrom().empty();
                }

                logger.info("Presentation {} didnt exists", presentation.getPresentationId());
                return this.getPresentationInformationAndStore(presentation.getPresentationId());
            });
    }

    public Uni<DrivePage> findPresentationsFromDrive(String pageToken) {
        return this.driveClient.findFiles(new Drive(MimeType.PRESENTATION, pageToken, 30));
    }

    public Uni<Presentation> findPresentationInformation(String presentationId) {
        return Uni.createFrom().item(presentationId)
            .onItem().transformToUni(presentation -> this.findPresentation(presentationId))
            .onItem().transformToUni(presentation -> {
                if (presentation.isExists()) {
                    logger.info("Presentation {} already exists", presentation.getPresentationId());
                    return this.slidesClient.getPresentation(presentation.getPresentationId());
                }

                logger.info("Presentation {} didnt exists", presentation.getPresentationId());
                return Uni.createFrom().nullItem();
            });
    }

    private Uni<BucketFile> getThumbnailBytes(Thumbnail thumbnail) {
        return Uni.createFrom().item(() -> {
            try {
                BufferedInputStream input = new BufferedInputStream(new URL(thumbnail.getContentUrl()).openStream());
                byte[] content = input.readAllBytes();
                return new BucketFile(thumbnail.getPresentationId() + "/" + thumbnail.getPageObjectId(), "image/png", content, thumbnail);
            } catch (IOException e) {
                // Melhor lançar uma excenção e trata-la em um middleware
                return null;
            }
        });
    }

    private Multi<BucketFile> getPresentationInformationAndStore(String presentation) {
        return Multi.createFrom().items(presentation)
            .onItem().transformToUniAndConcatenate(presentationId -> this.storePresentationOnDatabase(presentationId))
            .onItem().transformToMultiAndMerge(presentationId -> this.getAllThumbnails(presentationId))
            .onItem().transformToUniAndConcatenate(thumbnail -> this.getThumbnailBytes(thumbnail))
            .onItem().transformToUniAndConcatenate(file -> this.storeThumbnailOnStorage(file))
            .onItem().transformToUniAndConcatenate(file -> this.storeThumbnailOnDatabase(file));
    }

    private Uni<PresentationSearchResult> findPresentation(String presentationId) {
        return Uni.createFrom().item(presentationId)
            .onItem().transformToUni(presentation -> this.searchPresentationOnDatabase(presentation));
    }

    private Uni<BucketFile> storeThumbnailOnStorage(BucketFile file) {
        return Uni.createFrom().item(() -> this.googleCloudStorageResource.storage(file));
    }

    private Uni<String> storePresentationOnDatabase(String presentationId) {
        return Uni.createFrom().item(() -> this.googleFirestoreResource.storePresentation(presentationId));
    }

    private Uni<BucketFile> storeThumbnailOnDatabase(BucketFile file) {
        return Uni.createFrom().item(() -> this.googleFirestoreResource.storeThumbnail(file));
    }

    private Uni<PresentationSearchResult> searchPresentationOnDatabase(String presentationId) {
        return Uni.createFrom().item(() -> this.googleFirestoreResource.searchPresentation(presentationId));
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
