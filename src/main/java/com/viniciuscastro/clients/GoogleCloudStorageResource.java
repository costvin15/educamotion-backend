package com.viniciuscastro.clients;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.viniciuscastro.presentation.models.BucketFile;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/storage")
public class GoogleCloudStorageResource {
    @Inject
    Storage storage;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String storage(BucketFile file) {
        String bucketName = "educamotion-presentation-images";
        BlobId blobId = BlobId.of(bucketName, file.getFilename());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
            .setContentType(file.getContentType())
            .build();
        Blob blob = this.storage.create(blobInfo, file.getContent());

        return new String(blob.getContent());
    }
}
