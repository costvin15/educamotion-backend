package com.viniciuscastro.slides.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.viniciuscastro.slides.clients.GoogleDriveClient;
import com.viniciuscastro.slides.clients.GoogleSlidesClient;
import com.viniciuscastro.slides.models.Drive;
import com.viniciuscastro.slides.models.DriveFile;
import com.viniciuscastro.slides.models.DrivePage;
import com.viniciuscastro.slides.models.Slide;
import com.viniciuscastro.slides.models.SlideThumbnail;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;

@QuarkusTest
class SlidesServiceTest {
    @Inject
    SlidesService slidesService;

    @BeforeEach
    void setUp() {
        GoogleSlidesClient slidesClientMock = new GoogleSlidesClient() {
            @Override
            public Uni<SlideThumbnail> getThumbnail(String presentationId, String pageObjectId) {
                return Uni.createFrom().item(
                    SlideThumbnail
                        .builder()
                        .contentUrl("https://expectedContentUrl")
                        .width(1234)
                        .height(4321)
                        .build()
                );
            }

            @Override
            public Uni<Slide> getSlide(String presentationId) {
                Slide expectedSlide;

                switch (presentationId) {
                    case "expectedPresentationId-1":
                        expectedSlide = Slide
                            .builder()
                            .presentationId("expectedPresentationId-1")
                            .title("Expected Presentation 1")
                            .build();
                        break;
                    case "expectedPresentationId-2":
                        expectedSlide = Slide
                            .builder()
                            .presentationId("expectedPresentationId-2")
                            .title("Expected Presentation 2")
                            .build();
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected presentationId: " + presentationId);
                }

                return Uni.createFrom().item(expectedSlide);
            }
        };

        GoogleDriveClient driveClientMock = new GoogleDriveClient() {
            @Override
            public Uni<DrivePage> findFiles(Drive drive) {
                DriveFile file1 = DriveFile
                    .builder()
                    .id("expectedPresentationId-1")
                    .build();
                DriveFile file2 = DriveFile
                    .builder()
                    .id("expectedPresentationId-2")
                    .build();

                DrivePage drivePage = new DrivePage();

                if (drive.getPageToken() == null) {
                    drivePage.nextPageToken = "1";
                    drivePage.files = List.of(file1);
                } else {
                    drivePage.files = List.of(file2);
                }

                return Uni.createFrom().item(drivePage);
            }
        };

        QuarkusMock.installMockForType(slidesClientMock, GoogleSlidesClient.class, RestClient.LITERAL);
        QuarkusMock.installMockForType(driveClientMock, GoogleDriveClient.class, RestClient.LITERAL);
    }

    @Test
    void test_get_thumbnail_should_return_expected_thumbnail() {
        String presentationId = "presentationId";
        String pageObjectId = "pageObjectId";

        Uni<SlideThumbnail> result = this.slidesService.getThumbnail(presentationId, pageObjectId);
        SlideThumbnail receivedThumbnail = result.await().indefinitely();

        assertNotNull(receivedThumbnail);
        assertEquals("https://expectedContentUrl", receivedThumbnail.getContentUrl());
        assertEquals(1234, receivedThumbnail.getWidth());
        assertEquals(4321, receivedThumbnail.getHeight());
    }

    @ParameterizedTest
    @CsvSource({
        "0, expectedPresentationId-1, Expected Presentation 1",
        "1, expectedPresentationId-2, Expected Presentation 2"
    })
    void test_get_slides_should_return_expected_slides(int index, String expectedPresentationId, String expectedTitle) {
        Multi<Slide> result = this.slidesService.getSlides();
        List<Slide> receivedSlides = result.collect().asList().await().indefinitely();

        assertNotNull(receivedSlides);
        assertEquals(2, receivedSlides.size());
        assertEquals(expectedTitle, receivedSlides.get(index).getTitle());
        assertEquals(expectedPresentationId, receivedSlides.get(index).getPresentationId());
    }
}
