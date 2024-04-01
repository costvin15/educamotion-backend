package com.viniciuscastro.clients;

import java.io.ByteArrayInputStream;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.Blob.BlobSourceOption;
import com.viniciuscastro.exceptions.ApplicationException;
import com.viniciuscastro.exceptions.ApplicationException.StatusCode;
import com.viniciuscastro.presentation.models.BucketFile;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/storage")
public class GoogleCloudStorageResource {
    @Inject
    Storage storage;

    private static final String BUCKET_NAME = "educamotion-presentation-images";

    @POST
    @Path("storeFile")
    @Produces(MediaType.APPLICATION_JSON)
    public BucketFile storeFileOnImagesBucket(BucketFile file) {
        BlobId blobId = BlobId.of(BUCKET_NAME, file.getFilename());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
            .setContentType(file.getContentType())
            .build();
        this.storage.create(blobInfo, file.getContent());

        return file;
    }

    @POST
    @Path("getFile")
    @Produces(MediaType.APPLICATION_JSON)
    public ByteArrayInputStream getFileFromImagesBucket(BucketFile file) {
        try {
            BlobId blobId = BlobId.of(BUCKET_NAME, file.getFilename());
            Blob blob = this.storage.get(blobId);
            byte[] bytes = blob.getContent(BlobSourceOption.generationMatch());
            return new ByteArrayInputStream(bytes);
        } catch (StorageException exception) {
            throw new ApplicationException("Ocorreu um erro ao buscar imagem.", StatusCode.INTERNAL_SERVER_ERROR);
        }
    }
}
