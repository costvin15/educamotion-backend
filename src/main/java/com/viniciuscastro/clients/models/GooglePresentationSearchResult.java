package com.viniciuscastro.clients.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GooglePresentationSearchResult {
    private String presentationId;
    private boolean exists;
}
