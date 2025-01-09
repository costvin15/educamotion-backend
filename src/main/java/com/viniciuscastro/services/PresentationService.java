package com.viniciuscastro.services;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.viniciuscastro.dto.request.GoogleDriveSearchRequest;
import com.viniciuscastro.dto.response.GoogleDriveFileResponse;
import com.viniciuscastro.dto.response.GoogleDriveSearchResponse;
import com.viniciuscastro.dto.response.PresentationDetailResponse;
import com.viniciuscastro.dto.response.PresentationResponse;
import com.viniciuscastro.interfaces.GoogleCloudStorageInterface;
import com.viniciuscastro.interfaces.GoogleDriveInterface;
import com.viniciuscastro.interfaces.GoogleSlidesInterface;
import com.viniciuscastro.models.googleslide.GoogleSlide;
import com.viniciuscastro.models.googleslide.GoogleSlideImage;
import com.viniciuscastro.models.googleslide.Page;
import com.viniciuscastro.models.presentations.Presentation;
import com.viniciuscastro.models.presentations.SlidePage;
import com.viniciuscastro.repositories.PresentationRepository;
import com.viniciuscastro.repositories.SlidePageRepository;

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
    PresentationRepository presentationRepository;

    @Inject
    SlidePageRepository slidePageRepository;

    @Inject
    GoogleCloudStorageInterface googleCloudStorageInterface;

    @Inject
    @RestClient
    GoogleSlidesInterface googleSlidesInterface;

    @Inject
    @RestClient
    GoogleDriveInterface googleDriveInterface;

    public List<PresentationResponse> getPresentations() {
        List<Presentation> presentations = this.presentationRepository.findByUserId(
            this.userService.getUserId()
        );

        List<PresentationResponse> response = new ArrayList<>();

        for (Presentation presentation : presentations) {
            response.add(new PresentationResponse(
                presentation.getId(),
                presentation.getTitle(),
                this.googleCloudStorageInterface.fetchURL(presentation.getId()),
                presentation.getCreatedAt()
            ));
        }

        return response;
    }

    public List<PresentationResponse> getAvailablePresentations(String search) {
        GoogleDriveSearchRequest request = new GoogleDriveSearchRequest();
        request.setQ(String.format("mimeType='application/vnd.google-apps.presentation' and name contains '%s'", search));
        request.setPageSize(30);
        GoogleDriveSearchResponse driveResponse = this.googleDriveInterface.findFiles(request);
        List<PresentationResponse> response = new ArrayList<>();

        for (GoogleDriveFileResponse file : driveResponse.getFiles()) {
            response.add(new PresentationResponse(
                file.getId(),
                file.getName()
            ));
        }

        return response;
    }

    @Transactional
    public PresentationDetailResponse addPresentation(String presentationId) {
        GoogleSlide slide = this.googleSlidesInterface.getPresentation(presentationId);
        this.presentationRepository.persist(new Presentation(
            slide.getPresentationId(),
            slide.getTitle(),
            this.userService.getUserId(),
            Date.from(Instant.now())
        ));

        Optional<Presentation> presentation = this.presentationRepository.findById(presentationId);

        if (slide.getSlides().length > 0) {
            String pageId = slide.getSlides()[0].getObjectId();
            this.persistPresentationPageThumbnail(presentationId, presentationId, pageId);
        } else {
            // TODO: Add error handling
        }

        for (int i = 0; i < slide.getSlides().length; i++) {
            Page page = slide.getSlides()[i];
            String filename = String.format("%s/%s", presentationId, page.getObjectId());
            this.persistPresentationPageThumbnail(filename, presentationId, page.getObjectId());
            SlidePage slidePage = new SlidePage(
                presentation.get(),
                this.userService.getUserId(),
                page.getObjectId(),
                i,
                filename
            );
            this.slidePageRepository.persist(slidePage);
        }

        return this.getPresentationDetails(presentationId);
    }

    public PresentationDetailResponse getPresentationDetails(String presentationId) {
        Presentation presentation = this.getPresentation(presentationId);

        List<SlidePage> pages = this.slidePageRepository.findByPresentationId(presentation);
        String[] pagesIds = new String[pages.size()];
        for (SlidePage page : pages) {
            pagesIds[page.getSlideIndex()] = page.getSlideId();
        }

        return new PresentationDetailResponse(
            presentation.getId(),
            presentation.getTitle(),
            this.googleCloudStorageInterface.fetchURL(presentationId),
            presentation.getCreatedAt(),
            Arrays.asList(pagesIds),
            this.elementService.getElementsFromPresentation(presentationId)
        );
    }

    // TODO: Adicionar essa URL no retorno da rota do espectador
    public URL retrievePresentationThumbnail(String presentationId, String pageId) {
        return this.googleCloudStorageInterface.fetchURL(
            String.format("%s/%s", presentationId, pageId)
        );
    }

    public Presentation getPresentation(String presentationId) {
        return this.presentationRepository.findById(presentationId)
            .orElseThrow(() -> new IllegalArgumentException("Presentation not found"));
    }

    public boolean verifyIfPresentationExists(String presentationId) {
        try {
            this.getPresentation(presentationId);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public byte[] getStoredThumbnail(String presentationId, String pageId) {
        URL fileURL = this.googleCloudStorageInterface.fetchURL(
            String.format("%s/%s", presentationId, pageId)
        );

        try {
            BufferedInputStream input = new BufferedInputStream(fileURL.openStream());
            byte[] image = input.readAllBytes();
            input.close();
            return image;
        } catch (IOException e) {
            throw new IllegalArgumentException("Error fetching image");
        }
    }

    private boolean persistPresentationPageThumbnail(String fileName, String presentationId, String pageId) {
        byte[] thumbnail = this.getPresentationThumbnailFromSlidesAPI(presentationId, pageId);
        this.googleCloudStorageInterface.storeFile(fileName, "image/png", thumbnail);
        return true;
    }

    private byte[] getPresentationThumbnailFromSlidesAPI(String presentationId, String pageId) {
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
}
