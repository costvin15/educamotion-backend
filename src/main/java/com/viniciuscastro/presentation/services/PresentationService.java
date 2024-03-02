package com.viniciuscastro.presentation.services;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.viniciuscastro.clients.GoogleDriveClient;
import com.viniciuscastro.clients.GoogleSlidesClient;
import com.viniciuscastro.clients.models.Presentation;
import com.viniciuscastro.clients.models.PresentationThumbnail;
import com.viniciuscastro.clients.models.PresentationUpdate;
import com.viniciuscastro.clients.models.requests.CreateSlideRequest;
import com.viniciuscastro.clients.models.requests.Request;
import com.viniciuscastro.clients.models.requests.WriteControl;
import com.viniciuscastro.presentation.models.Drive;
import com.viniciuscastro.presentation.models.DrivePage;
import com.viniciuscastro.presentation.resources.MimeType;

import io.quarkus.cache.CacheResult;
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

    @CacheResult(cacheName = "findFirstThumbnailFromPresentation")
    public Uni<PresentationThumbnail> getThumbnail(String presentationId) {
        Uni<Presentation> slideInformation = this.findPresentationInformation(presentationId);
        return slideInformation.onItem().transformToUni(presentation -> {
            if (presentation.getSlides() == null || presentation.getSlides().isEmpty()) {
                return Uni.createFrom().nullItem();
            }
            return this.slidesClient.getPresentationThumbnail(presentationId, presentation.getSlides().get(0).getObjectId());
        });
    }

    @CacheResult(cacheName = "findAllThumbnailsFromPresentation")
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

    @CacheResult(cacheName = "getPresentationInformation")
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
    public String createSlide(String presentationId) {
        Uni<Presentation> presentationInformation = this.findPresentationInformation(presentationId);
        Presentation presentation = presentationInformation.await().indefinitely();

        CreateSlideRequest createSlideRequest = new CreateSlideRequest(null, 0, null, null);

        Request request = new Request(createSlideRequest);
        Request[] requests = { request };
        WriteControl writeControl = new WriteControl(presentation.getRevisionId());

        PresentationUpdate presentationUpdate = new PresentationUpdate(requests, writeControl);
        return this.slidesClient.performBatchUpdate(presentationId, presentationUpdate).await().indefinitely();
    }
}
