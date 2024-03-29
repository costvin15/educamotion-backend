package com.viniciuscastro.clients.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StorePresentationRequest {
    private String presentationId;
    private String userId;
}
