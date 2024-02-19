package com.viniciuscastro.slides.services;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.viniciuscastro.slides.clients.GoogleDriveClient;
import com.viniciuscastro.slides.clients.GoogleSlidesClient;
import com.viniciuscastro.slides.models.Drive;
import com.viniciuscastro.slides.models.DriveFile;
import com.viniciuscastro.slides.models.DrivePage;
import com.viniciuscastro.slides.models.Slide;
import com.viniciuscastro.slides.models.SlideThumbnail;
import com.viniciuscastro.slides.resources.MimeType;

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

    public Uni<SlideThumbnail> getThumbnail(String presentationId, String pageObjectId) {
        return this.slidesClient.getThumbnail(presentationId, pageObjectId);
    }

    public Multi<Slide> getSlides() {
        Multi<List<DriveFile>> pages = this.findAllPresentationsFromDrive()
            .onItem().transform(page -> page.files);
        Multi<DriveFile> files = pages.flatMap(list -> {
            return Multi.createFrom().iterable(list);
        });
        Multi<String> presentationsIds = files.onItem().transform(file -> file.id);
        Multi<Slide> slides = presentationsIds.onItem()
            .transformToUni(presentationId -> this.findSlideInformation(presentationId))
            .merge();
        return slides;
    }

    private Uni<Slide> findSlideInformation(String presentationId) {
        return this.slidesClient.getSlide(presentationId);
    }

    public Multi<DrivePage> findAllPresentationsFromDrive() {
        Multi<DrivePage> stream = Multi.createBy().repeating()
            .uni(
                () -> new AtomicReference<String>(null),
                state -> driveClient.findFiles(new Drive(MimeType.PRESENTATION, state.get()))
                    .onItem().invoke(page -> state.set(page.nextPageToken))
            )
            .whilst(page -> page.nextPageToken != null);
        return stream;
    }
}
