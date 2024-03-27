package com.viniciuscastro.presentation.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Thumbnail {
    private String presentationId;
    private String pageObjectId;
    private String contentUrl;
}
