package com.viniciuscastro.presentation.services;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.viniciuscastro.presentation.controllers.PresentationController;
import com.viniciuscastro.presentation.models.Drive;
import com.viniciuscastro.presentation.models.DrivePage;
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

    Logger logger = LoggerFactory.getLogger(PresentationController.class);

    public PresentationThumbnail getThumbnailBlocking(String presentationId) {
        Presentation presentation = this.slidesClient.getPresentationBlocking(presentationId);
        if (presentation.getSlides() == null || presentation.getSlides().isEmpty()) {
            return null;
        }
        PresentationThumbnail thumbnail = this.slidesClient.getPresentationThumbnailBlocking(presentationId, presentation.getSlides().get(0).getObjectId());

        logger.info(thumbnail.getContentUrl());
        try {
            BufferedInputStream input = new BufferedInputStream(new URL(thumbnail.getContentUrl()).openStream());
            byte[] content = input.readAllBytes();
            this.googleCloudStorageResource.storage(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return thumbnail;
    }

    public Uni<PresentationThumbnail> getThumbnail(String presentationId) {
        Uni<Presentation> slideInformation = this.findPresentationInformation(presentationId);
        return slideInformation.onItem().transformToUni(presentation -> {
            if (presentation.getSlides() == null || presentation.getSlides().isEmpty()) {
                return Uni.createFrom().nullItem();
            }
            return this.slidesClient.getPresentationThumbnail(presentationId, presentation.getSlides().get(0).getObjectId());
        });
    }

    public Multi<PresentationThumbnail> getAllThumbnails(String presentationId) {
        Uni<Presentation> slideInformation = this.findPresentationInformation(presentationId);
        Multi<PresentationThumbnail> thumbnails = slideInformation.onItem().transformToMulti(presentation -> {
            if (presentation.getSlides() == null || presentation.getSlides().isEmpty()) {
                return Multi.createFrom().empty();
            }
            
            return Multi.createFrom().iterable(presentation.getSlides())
                .onItem()
                .transformToUni(slideItem -> this.slidesClient.getPresentationThumbnail(presentationId, slideItem.getObjectId()))
                .merge();
        });
        return thumbnails;
    }

    public Uni<Presentation> findPresentationInformation(String presentationId) {
        return this.slidesClient.getPresentation(presentationId);
    }

    public Uni<DrivePage> findPresentationsFromDrive(String pageToken) {
        return this.driveClient.findFiles(new Drive(MimeType.PRESENTATION, pageToken, 30));
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
