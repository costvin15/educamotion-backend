package com.viniciuscastro.slides.services;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.viniciuscastro.slides.clients.GoogleDriveClient;
import com.viniciuscastro.slides.clients.GoogleSlidesClient;
import com.viniciuscastro.slides.models.Drive;
import com.viniciuscastro.slides.models.DrivePage;
import com.viniciuscastro.slides.models.Slide;
import com.viniciuscastro.slides.models.SlideThumbnail;
import com.viniciuscastro.slides.resources.MimeType;

import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class SlidesService {
    @Inject
    @RestClient
    GoogleDriveClient driveClient;
    
    @Inject
    @RestClient
    GoogleSlidesClient slidesClient;

    @CacheResult(cacheName = "slideThumbnail")
    public Uni<SlideThumbnail> getThumbnail(String presentationId) {
        Uni<Slide> slideInformation = this.findSlideInformation(presentationId);
        return slideInformation.onItem().transformToUni(slide -> {
            if (slide.getSlides() == null || slide.getSlides().isEmpty()) {
                return Uni.createFrom().nullItem();
            }
            return this.slidesClient.getThumbnail(presentationId, slide.getSlides().get(0).getObjectId());
        });
    }

    @CacheResult(cacheName = "slideAllThumbnails")
    public Multi<SlideThumbnail> getAllThumbnails(String presentationId) {
        Uni<Slide> slideInformation = this.findSlideInformation(presentationId);
        Multi<SlideThumbnail> thumbnails = slideInformation.onItem().transformToMulti(slide -> {
            if (slide.getSlides() == null || slide.getSlides().isEmpty()) {
                return Multi.createFrom().empty();
            }
            
            return Multi.createFrom().iterable(slide.getSlides())
                .onItem()
                .transformToUni(slideItem -> this.slidesClient.getThumbnail(presentationId, slideItem.getObjectId()))
                .merge();
        });
        return thumbnails;
    }

    @CacheResult(cacheName = "slideInformation")
    public Uni<Slide> findSlideInformation(String presentationId) {
        return this.slidesClient.getSlide(presentationId);
    }

    public Uni<DrivePage> findPresentationsFromDrive(String pageToken) {
        return this.driveClient.findFiles(new Drive(MimeType.PRESENTATION, pageToken, 30));
    }
}
