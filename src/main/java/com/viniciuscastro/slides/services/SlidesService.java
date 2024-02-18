package com.viniciuscastro.slides.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.viniciuscastro.slides.clients.GoogleDriveClient;
import com.viniciuscastro.slides.clients.GoogleSlidesClient;
import com.viniciuscastro.slides.models.Drive;
import com.viniciuscastro.slides.models.DrivePage;
import com.viniciuscastro.slides.models.Slide;
import com.viniciuscastro.slides.resources.MimeType;

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

    @SuppressWarnings("unchecked")
    public Uni<List<Slide>> getSlides() {
        List<String> presentationIds = this.findPresentationsIds();
        List<Uni<Slide>> slides = new ArrayList<>();

        for (String presentationId : presentationIds) {
            slides.add(this.findSlideInformation(presentationId));
        }

        return Uni.combine()
            .all()
            .unis(slides)
            .with(list -> (List<Slide>) list);
    }

    private Uni<Slide> findSlideInformation(String presentationId) {
        return this.slidesClient.getSlide(presentationId);
    }

    private List<String> findPresentationsIds() {
        List<String> presentationIds = new ArrayList<>();

        DrivePage drivePage = this.driveClient.findFiles(new Drive(MimeType.PRESENTATION));
        String nextPageToken = drivePage.nextPageToken;

        while (nextPageToken != null) {
            List<String> foundedIds = drivePage.files.stream()
                .map(file -> file.id)
                .collect(Collectors.toList());
            presentationIds.addAll(foundedIds);
            drivePage = this.driveClient.findFiles(new Drive(MimeType.PRESENTATION, nextPageToken));
            nextPageToken = drivePage.nextPageToken;
        }

        return presentationIds;
    }
}
