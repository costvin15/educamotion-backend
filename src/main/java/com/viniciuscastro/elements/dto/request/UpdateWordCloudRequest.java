package com.viniciuscastro.elements.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateWordCloudRequest {
    private String id;
    private String title;
    private Boolean enableMultipleEntries;
}
