package com.viniciuscastro.presentation.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.viniciuscastro.presentation.clients.GoogleDriveClient;
import com.viniciuscastro.presentation.clients.GoogleSlidesClient;
import com.viniciuscastro.presentation.models.Drive;
import com.viniciuscastro.presentation.models.DriveFile;
import com.viniciuscastro.presentation.models.DrivePage;
import com.viniciuscastro.presentation.models.Presentation;
import com.viniciuscastro.presentation.models.PresentationPage;
import com.viniciuscastro.presentation.models.PresentationThumbnail;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;

@QuarkusTest
class PresentationServiceTest {
    @Inject
    PresentationService slidesService;

    @BeforeEach
    void setUp() {
        GoogleSlidesClient slidesClientMock = new GoogleSlidesClient() {
            @Override
            public Uni<PresentationThumbnail> getThumbnail(String presentationId, String pageObjectId) {
                return Uni.createFrom().item(
                    PresentationThumbnail
                        .builder()
                        .contentUrl("https://expectedContentUrl")
                        .width(1234)
                        .height(4321)
                        .build()
                );
            }

            @Override
            public Uni<Presentation> getSlide(String presentationId) {
                Presentation expectedSlide;

                switch (presentationId) {
                    case "expectedPresentationId-1":
                        expectedSlide = Presentation
                            .builder()
                            .presentationId("expectedPresentationId-1")
                            .title("Expected Presentation 1")
                            .slides(List.of(
                                PresentationPage.builder()
                                    .objectId("p")
                                    .build()
                            ))
                            .build();
                        break;
                    case "expectedPresentationId-2":
                        expectedSlide = Presentation
                            .builder()
                            .presentationId("expectedPresentationId-2")
                            .title("Expected Presentation 2")
                            .slides(List.of(
                                PresentationPage.builder()
                                    .objectId("b")
                                    .build()
                            ))
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
                    .name("Expected Presentation 1")
                    .build();
                DriveFile file2 = DriveFile
                    .builder()
                    .id("expectedPresentationId-2")
                    .name("Expected Presentation 2")
                    .build();

                DrivePage drivePage;

                if (drive.getPageToken() == null) {
                    drivePage = DrivePage.builder()
                        .nextPageToken("1")
                        .files(List.of(file1))
                        .build();
                } else {
                    drivePage = DrivePage.builder()
                        .files(List.of(file2))
                        .build();
                }

                return Uni.createFrom().item(drivePage);
            }
        };

        QuarkusMock.installMockForType(slidesClientMock, GoogleSlidesClient.class, RestClient.LITERAL);
        QuarkusMock.installMockForType(driveClientMock, GoogleDriveClient.class, RestClient.LITERAL);
    }

    @Test
    void test_get_thumbnail_should_return_expected_thumbnail() {
        String presentationId = "expectedPresentationId-1";

        Uni<PresentationThumbnail> result = this.slidesService.getThumbnail(presentationId);
        PresentationThumbnail receivedThumbnail = result.await().indefinitely();

        assertNotNull(receivedThumbnail);
        assertEquals("https://expectedContentUrl", receivedThumbnail.getContentUrl());
        assertEquals(1234, receivedThumbnail.getWidth());
        assertEquals(4321, receivedThumbnail.getHeight());
    }

    @ParameterizedTest
    @CsvSource({
        ", expectedPresentationId-1, Expected Presentation 1",
        "1, expectedPresentationId-2, Expected Presentation 2"
    })
    void test_get_slides_should_return_expected_slides(String nextPageToken, String expectedPresentationId, String expectedTitle) {
        Uni<DrivePage> result = this.slidesService.findPresentationsFromDrive(nextPageToken);
        DrivePage receivedPage = result.await().indefinitely();

        assertNotNull(receivedPage);
        assertEquals(1, receivedPage.getFiles().size());
        assertEquals(expectedTitle, receivedPage.getFiles().get(0).getName());
        assertEquals(expectedPresentationId, receivedPage.getFiles().get(0).getId());
    }
}
