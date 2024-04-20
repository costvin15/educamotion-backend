package com.viniciuscastro.presentation.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.cloud.Timestamp;
import com.viniciuscastro.clients.GoogleSlidesClient;
import com.viniciuscastro.clients.PresentationFirestoreResource;
import com.viniciuscastro.clients.models.GooglePresentation;
import com.viniciuscastro.clients.models.GooglePresentationSearchResult;
import com.viniciuscastro.clients.models.GoogleThumbnail;
import com.viniciuscastro.clients.models.requests.PresentationUpdateRequest;
import com.viniciuscastro.clients.models.responses.PresentationBatchUpdateResponse;
import com.viniciuscastro.exceptions.ApplicationException;
import com.viniciuscastro.presentation.dto.response.PresentationWithSlidesResponse;
import com.viniciuscastro.presentation.models.PresentationPage;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import jakarta.inject.Inject;

@QuarkusTest
class PresentationServiceTest {
    @Inject
    PresentationService slidesService;

    @BeforeAll
    public static void setup() {
        PresentationFirestoreResource firestoreResourceMock = Mockito.mock(PresentationFirestoreResource.class);
        Mockito.when(firestoreResourceMock.searchPresentation(eq("presentation-id-1")))
            .thenReturn(
                GooglePresentationSearchResult.builder()
                    .presentationId("presentation-id-1")
                    .createdAt(Timestamp.of(new Date(1704081661)))
                    .updatedAt(Timestamp.of(new Date(1704081661)))
                    .exists(true)
                    .build()
            );
        Mockito.when(firestoreResourceMock.searchPresentation(eq("presentation-id-2")))
            .thenReturn(
                GooglePresentationSearchResult.builder()
                    .presentationId("presentation-id-2")
                    .createdAt(Timestamp.of(new Date(1704081661)))
                    .updatedAt(Timestamp.of(new Date(1704081661)))
                    .exists(true)
                    .build()
            );
        Mockito.when(firestoreResourceMock.searchPresentation(eq("presentation-id-3")))
            .thenReturn(
                GooglePresentationSearchResult.builder()
                    .presentationId("presentation-id-3")
                    .createdAt(Timestamp.of(new Date(1704081661)))
                    .updatedAt(Timestamp.of(new Date(1704081661)))
                    .exists(true)
                    .build()
            );
        QuarkusMock.installMockForType(firestoreResourceMock, PresentationFirestoreResource.class);

        GoogleSlidesClient googleSlidesClientMock = new GoogleSlidesClient() {
            @Override
            public Uni<GooglePresentation> getPresentation(String presentationId) {
                List<PresentationPage> slides = new ArrayList<>();
                if (presentationId == "presentation-id-1") {
                    slides.add(PresentationPage.builder()
                        .objectId("expected-object-id")
                        .build());
                }
                if (presentationId == "presentation-id-2") {
                    slides = null;
                }

                return Uni.createFrom().item(GooglePresentation.builder()
                    .presentationId(presentationId)
                    .title("expected-title")
                    .revisionId("expected-revision-id")
                    .slides(slides)
                    .build());
            }

            @Override
            public Uni<GoogleThumbnail> getPresentationThumbnail(String presentationId, String pageObjectId) {
                return null;
            }

            @Override
            public Uni<PresentationBatchUpdateResponse> performBatchUpdate(String presentationId, PresentationUpdateRequest presentationUpdate) {
                return null;
            }
        };
        QuarkusMock.installMockForType(googleSlidesClientMock, GoogleSlidesClient.class, RestClient.LITERAL);
    }

    @Test
    void teste_buscar_apresentacao_por_id_deve_retornar_dados_esperados() {
        Uni<PresentationWithSlidesResponse> presentation = slidesService.getPresentationById("presentation-id-1");

        UniAssertSubscriber<PresentationWithSlidesResponse> subscriber = presentation
            .invoke(receivedPresentation -> assertEquals("presentation-id-1", receivedPresentation.getPresentationId()))
            .invoke(receivedPresentation -> assertEquals("expected-title", receivedPresentation.getTitle()))
            .invoke(receivedPresentation -> assertEquals(1, receivedPresentation.getTotalSlides()))
            .invoke(receivedPresentation -> assertEquals("expected-object-id", receivedPresentation.getSlides().get(0).getObjectId()))
            .subscribe().withSubscriber(UniAssertSubscriber.create());
        subscriber.assertCompleted();
    }

    @Test
    void teste_buscar_apresentacao_por_id_com_slides_nulos_deve_retornar_erro_esperado() {
        Uni<PresentationWithSlidesResponse> presentation = slidesService.getPresentationById("presentation-id-2");

        UniAssertSubscriber<PresentationWithSlidesResponse> subscriber = presentation
            .subscribe().withSubscriber(UniAssertSubscriber.create());
        subscriber.assertFailedWith(ApplicationException.class, "Apresentação não possui slides.");
    }

    @Test
    void teste_buscar_apresentacao_por_id_com_slides_vazios_deve_retornar_erro_esperado() {
        Uni<PresentationWithSlidesResponse> presentation = slidesService.getPresentationById("presentation-id-3");

        UniAssertSubscriber<PresentationWithSlidesResponse> subscriber = presentation
            .subscribe().withSubscriber(UniAssertSubscriber.create());
        subscriber.assertFailedWith(ApplicationException.class, "Apresentação não possui slides.");
    }
}
