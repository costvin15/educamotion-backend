package com.viniciuscastro.clients;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

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
    public String storage(byte[] content) {
        String bucketName = "educamotion-presentation-images";
        BlobId blobId = BlobId.of(bucketName, "Thumbnail.png");
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
            .setContentType("image/png")
            .build();
        Blob blob = this.storage.create(blobInfo, content);

        return new String(blob.getContent());
    }
}
