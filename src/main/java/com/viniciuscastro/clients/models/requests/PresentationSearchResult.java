package com.viniciuscastro.clients.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresentationSearchResult {
    private String presentationId;
    private boolean exists;
}
