package com.viniciuscastro.presentation.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BucketFile {
    private String filename;
    private String contentType;
    private byte[] content;
}
