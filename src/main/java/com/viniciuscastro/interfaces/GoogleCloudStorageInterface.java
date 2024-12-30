package com.viniciuscastro.interfaces;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.SignUrlOption;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class File {
    private String fileName;
    private String contentType;
    private byte[] file;
}

@Path("storage")
@RequestScoped
public class GoogleCloudStorageInterface {
    @Inject
    Storage storage;

    private static final String BUCKET_NAME = "educamotion-presentation-images";

    @POST
    @Path("store")
    @Produces(MediaType.APPLICATION_JSON)
    public void performStore(File file) {
        BlobId blobId = BlobId.of(BUCKET_NAME, file.getFileName());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
            .setContentType(file.getContentType())
            .build();
        storage.create(blobInfo, file.getFile());
    }

    public URL fetchURL(String filename) {
        BlobId blobId = BlobId.of(BUCKET_NAME, filename);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        URL url = storage.signUrl(blobInfo, 100, TimeUnit.SECONDS, SignUrlOption.withV4Signature());
        return url;
    }

    public void storeFile(String fileName, String contentType, byte[] fileContent) {
        File file = new File(fileName, contentType, fileContent);
        this.performStore(file);
    }
}
