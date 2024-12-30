package com.viniciuscastro.services;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.viniciuscastro.dto.response.PresentationDetailResponse;
import com.viniciuscastro.dto.response.PresentationResponse;
import com.viniciuscastro.interfaces.GoogleCloudStorageInterface;
import com.viniciuscastro.interfaces.GoogleSlidesInterface;
import com.viniciuscastro.models.googleslide.GoogleSlide;
import com.viniciuscastro.models.googleslide.GoogleSlideImage;
import com.viniciuscastro.models.googleslide.Page;
import com.viniciuscastro.models.presentations.Presentation;
import com.viniciuscastro.repositories.PresentationRepository;

import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class PresentationService {
    @Inject
    UserService userService;

    @Inject
    ElementService elementService;

    @Inject
    PresentationRepository repository;

    @Inject
    @RestClient
    GoogleSlidesInterface googleSlidesInterface;

    @Inject
    GoogleCloudStorageInterface googleCloudStorageInterface;

    public List<PresentationResponse> getPresentations() {
        List<Presentation> presentations = this.repository.findByUserId(
            this.userService.getUserId()
        );

        List<PresentationResponse> response = new ArrayList<>();

        for (Presentation presentation : presentations) {
            response.add(new PresentationResponse(
                presentation.getId(),
                presentation.getTitle(),
                this.googleCloudStorageInterface.fetchURL(presentation.getId()),
                presentation.getCreated_at()
            ));
        }

        return response;
    }

    @Transactional
    public PresentationDetailResponse addPresentation(String presentationId) {
        if (this.verifyIfPresentationExists(presentationId)) {
            // TODO: Add error handling
            throw new IllegalArgumentException("Presentation already exists");
        }

        GoogleSlide slide = this.googleSlidesInterface.getPresentation(presentationId);
        this.repository.persist(new Presentation(
            slide.getPresentationId(),
            slide.getTitle(),
            this.userService.getUserId(),
            Date.from(Instant.now())
        ));

        for (Page page : slide.getSlides()) {
            this.persistPresentationPageThumbnail(
                String.format("%s/%s", presentationId, page.getObjectId()),
                presentationId,
                page.getObjectId()
            );
        }

        if (slide.getSlides().length > 0) {
            String pageId = slide.getSlides()[0].getObjectId();
            this.persistPresentationPageThumbnail(presentationId, presentationId, pageId);
        } else {
            // TODO: Add error handling
        }

        return this.getPresentationDetails(presentationId);
    }

    public PresentationDetailResponse getPresentationDetails(String presentationId) {
        Presentation presentation = this.repository.findById(presentationId, this.userService.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("Presentation not found"));

        return new PresentationDetailResponse(
            presentation.getId(),
            presentation.getTitle(),
            this.googleCloudStorageInterface.fetchURL(presentationId),
            presentation.getCreated_at(),
            this.elementService.getElementsFromPresentation(presentationId)
        );
    }

    @CacheResult(cacheName = "get-thumbnail-from-presentation-page")
    public byte[] getPresentationThumbnail(String presentationId, String pageId) {
        GoogleSlide slide = this.googleSlidesInterface.getPresentation(presentationId);
        Page[] pages = slide.getSlides();
        if (pages.length == 0) {
            throw new IllegalArgumentException("Presentation has no pages");
        }
        GoogleSlideImage slideImage = this.googleSlidesInterface.getPresentationImage(presentationId, pageId);
        String contentUrl = slideImage.getContentUrl();

        try {
            BufferedInputStream input = new BufferedInputStream(new URL(contentUrl).openStream());
            byte[] image = input.readAllBytes();
            input.close();
            return image;
        } catch (IOException e) {
            throw new IllegalArgumentException("Error fetching image");
        }
    }

    private boolean verifyIfPresentationExists(String presentationId) {
        return this.repository.findById(presentationId, this.userService.getUserId())
            .isPresent();
    }

    private boolean persistPresentationPageThumbnail(String fileName, String presentationId, String pageId) {
        byte[] thumbnail = this.getPresentationThumbnail(presentationId, pageId);
        this.googleCloudStorageInterface.storeFile(fileName, "image/png", thumbnail);
        return true;
    }
}
