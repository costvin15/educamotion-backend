package com.viniciuscastro.presentation.services;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.viniciuscastro.presentation.clients.GoogleDriveClient;
import com.viniciuscastro.presentation.clients.GoogleSlidesClient;
import com.viniciuscastro.presentation.models.Drive;
import com.viniciuscastro.presentation.models.DrivePage;
import com.viniciuscastro.presentation.models.Presentation;
import com.viniciuscastro.presentation.models.PresentationThumbnail;
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
            return this.slidesClient.getThumbnail(presentationId, presentation.getSlides().get(0).getObjectId());
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
                .transformToUni(slideItem -> this.slidesClient.getThumbnail(presentationId, slideItem.getObjectId()))
                .merge();
        });
        return thumbnails;
    }

    @CacheResult(cacheName = "getPresentationInformation")
    public Uni<Presentation> findPresentationInformation(String presentationId) {
        return this.slidesClient.getSlide(presentationId);
    }

    public Uni<DrivePage> findPresentationsFromDrive(String pageToken) {
        return this.driveClient.findFiles(new Drive(MimeType.PRESENTATION, pageToken, 30));
    }
}
