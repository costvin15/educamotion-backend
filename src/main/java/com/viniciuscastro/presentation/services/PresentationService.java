package com.viniciuscastro.presentation.services;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.viniciuscastro.clients.GoogleCloudStorageResource;
import com.viniciuscastro.clients.GoogleDriveClient;
import com.viniciuscastro.clients.GoogleFirestoreResource;
import com.viniciuscastro.clients.GoogleSlidesClient;
import com.viniciuscastro.clients.models.GooglePresentation;
import com.viniciuscastro.clients.models.GooglePresentationSearchResult;
import com.viniciuscastro.clients.models.GoogleThumbnail;
import com.viniciuscastro.clients.models.WriteControlBody;
import com.viniciuscastro.clients.models.requests.CreateSlideBodyRequest;
import com.viniciuscastro.clients.models.requests.SlideLayoutReference;
import com.viniciuscastro.clients.models.requests.CreateSlideRequest;
import com.viniciuscastro.clients.models.requests.PresentationUpdateRequest;
import com.viniciuscastro.clients.models.requests.SlideLayoutReference.PredefinedLayout;
import com.viniciuscastro.clients.models.responses.PresentationUpdateResponse;
import com.viniciuscastro.exceptions.ApplicationException;
import com.viniciuscastro.exceptions.ApplicationException.StatusCode;
import com.viniciuscastro.presentation.dto.response.ImportResultResponse;
import com.viniciuscastro.presentation.dto.response.Presentation;
import com.viniciuscastro.presentation.models.BucketFile;
import com.viniciuscastro.presentation.models.Drive;
import com.viniciuscastro.presentation.models.DrivePage;
import com.viniciuscastro.presentation.models.Thumbnail;
import com.viniciuscastro.presentation.resources.MimeType;

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

    @Inject
    GoogleCloudStorageResource googleCloudStorageResource;

    @Inject
    GoogleFirestoreResource googleFirestoreResource;

    Logger logger = LoggerFactory.getLogger(PresentationService.class);

    public Uni<DrivePage> findPresentationsAvailableForImport(String pageToken) {
        return this.driveClient.findFiles(new Drive(MimeType.PRESENTATION, pageToken, 30));
    }

    public Uni<Presentation> getPresentationById(String presentationId) {
        return Uni.createFrom().item(presentationId)
            .onItem().transformToUni(presentation -> this.getImportedPresentationInformation(presentationId))
            .onItem().transformToUni(presentation -> {
                if (presentation.getSlides() == null || presentation.getSlides().isEmpty()) {
                    throw new ApplicationException("Apresentação não possui slides.", StatusCode.NO_CONTENT);
                }

                String objectId = presentation.getSlides().get(0).getObjectId();
                // TODO: Atualmente isto esta utilizando uma rota com limitação de uso. É necessário
                // refatorar para utilização da rota do Bucket. 
                return this.slidesClient.getPresentationThumbnail(presentation.getPresentationId(), objectId)
                    .onItem().transformToUni(thumbnail -> {
                        Presentation response = new Presentation(presentationId, presentation.getTitle(), thumbnail.getContentUrl());
                        return Uni.createFrom().item(response);
                    });
            });
    }

    public Uni<ImportResultResponse> importPresentations(String[] presentationIds) {
        return Multi.createFrom().items(presentationIds)
            .onItem().transformToUniAndConcatenate(presentationId -> this.findPresentation(presentationId))
            .onItem().transformToMultiAndConcatenate(presentation -> {
                if (presentation.isExists()) {
                    return Multi.createFrom().empty();
                }

                return this.getPresentationInformationAndStore(presentation.getPresentationId());
            })
            .collect().asList()
            .onItem().transformToUni(result -> Uni.createFrom().item(new ImportResultResponse(result.size())));
    }

    public Multi<GooglePresentation> searchAllImportedPresentations() {
        return Multi.createFrom().items(this.googleFirestoreResource.searchAllImportedPresentations())
            .onItem().transformToUniAndConcatenate(presentation -> this.slidesClient.getPresentation(presentation.getPresentationId()));
    }

    private Uni<GooglePresentation> getImportedPresentationInformation(String presentationId) {
        return Uni.createFrom().item(presentationId)
            .onItem().transformToUni(presentation -> this.findPresentation(presentationId))
            .onItem().transformToUni(presentation -> {
                if (presentation.isExists()) {
                    return this.slidesClient.getPresentation(presentation.getPresentationId());
                }

                throw new ApplicationException("Apresentação não encontrada.", StatusCode.NOT_FOUND);
            });
    }

    private Uni<BucketFile> getThumbnailBytes(Thumbnail thumbnail) {
        return Uni.createFrom().item(() -> {
            try {
                BufferedInputStream input = new BufferedInputStream(new URL(thumbnail.getContentUrl()).openStream());
                byte[] content = input.readAllBytes();
                return new BucketFile(thumbnail.getPresentationId() + "/" + thumbnail.getPageObjectId(), "image/png", content, thumbnail);
            } catch (IOException e) {
                throw new ApplicationException("Ocorreu um erro ao importar a imagem.", StatusCode.INTERNAL_SERVER_ERROR);
            }
        });
    }

    private Multi<BucketFile> getPresentationInformationAndStore(String presentation) {
        return Multi.createFrom().items(presentation)
            .onItem().transformToUniAndConcatenate(presentationId -> this.storePresentationOnDatabase(presentationId))
            .onItem().transformToMultiAndMerge(presentationId -> this.getAllThumbnails(presentationId))
            .onItem().transformToUniAndConcatenate(thumbnail -> this.getThumbnailBytes(thumbnail))
            .onItem().transformToUniAndConcatenate(file -> this.storeThumbnailOnStorage(file))
            .onItem().transformToUniAndConcatenate(file -> this.storeThumbnailOnDatabase(file));
    }

    private Uni<GooglePresentationSearchResult> findPresentation(String presentationId) {
        return Uni.createFrom().item(presentationId)
            .onItem().transformToUni(presentation -> this.searchPresentationOnDatabase(presentation));
    }

    private Uni<BucketFile> storeThumbnailOnStorage(BucketFile file) {
        return Uni.createFrom().item(() -> this.googleCloudStorageResource.storage(file));
    }

    private Uni<String> storePresentationOnDatabase(String presentationId) {
        return Uni.createFrom().item(() -> this.googleFirestoreResource.storePresentation(presentationId));
    }

    private Uni<BucketFile> storeThumbnailOnDatabase(BucketFile file) {
        return Uni.createFrom().item(() -> this.googleFirestoreResource.storeThumbnail(file));
    }

    private Uni<GooglePresentationSearchResult> searchPresentationOnDatabase(String presentationId) {
        return Uni.createFrom().item(() -> this.googleFirestoreResource.searchPresentation(presentationId));
    }

    private Multi<Thumbnail> getAllThumbnails(String presentationId) {
        Uni<GooglePresentation> slideInformation = this.getImportedPresentationInformation(presentationId);
        return slideInformation.onItem().transformToMulti(presentation -> {
            if (presentation.getSlides() == null || presentation.getSlides().isEmpty()) {
                return Multi.createFrom().empty();
            }
            
            return Multi.createFrom().iterable(presentation.getSlides())
                .onItem()
                .transformToUni(slideItem -> {
                    String objectId = slideItem.getObjectId();
                    Uni<GoogleThumbnail> presentationThumbnail = this.slidesClient.getPresentationThumbnail(presentationId, slideItem.getObjectId());
                    return presentationThumbnail.onItem().transformToUni(thumbnail -> {
                        return Uni.createFrom().item(() -> new Thumbnail(presentationId, objectId, thumbnail.getContentUrl()));
                    });
                })
                .merge();
        });
    }

    /**
     * TODO: Há um caso de uso que não está sendo atendido, que é quando o usuário não tem permissão para
     * editar esse slide (Por exemplo: O slide foi compartilhado com ele apenas para visualização)
     */
    public Uni<PresentationUpdateResponse> createSlidePage(String presentationId) {
        Uni<GooglePresentation> presentationInformation = this.getImportedPresentationInformation(presentationId);
        return presentationInformation.onItem().transformToUni(presentation -> {
            SlideLayoutReference layoutReference = new SlideLayoutReference(PredefinedLayout.TITLE_AND_TWO_COLUMNS, null);
            CreateSlideBodyRequest createSlideRequest = new CreateSlideBodyRequest(null, 0, layoutReference, null);
    
            CreateSlideRequest request = new CreateSlideRequest(createSlideRequest);
            CreateSlideRequest[] requests = { request };
            WriteControlBody writeControl = new WriteControlBody(presentation.getRevisionId());
    
            PresentationUpdateRequest presentationUpdate = new PresentationUpdateRequest(requests, writeControl);
            return this.slidesClient.performBatchUpdate(presentationId, presentationUpdate);
        });
    }
}
