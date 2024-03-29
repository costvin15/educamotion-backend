package com.viniciuscastro.presentation.models;

import lombok.Getter;

@Getter
public class BucketFile {
    private String filename;
    private String contentType;
    private byte[] content;
    private Thumbnail thumbnail;

    public BucketFile(String presentationId, String objectId) {
        this.filename = String.format("%s/%s", presentationId, objectId);
    }

    public BucketFile(String presentationId, String objectId, byte[] content, Thumbnail thumbnail) {
        this.filename = String.format("%s/%s", presentationId, objectId);
        this.contentType = "image/png";
        this.content = content;
        this.thumbnail = thumbnail;
    }
}
