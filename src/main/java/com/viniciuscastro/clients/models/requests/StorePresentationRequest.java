package com.viniciuscastro.clients.models.requests;

import com.google.cloud.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class StorePresentationRequest {
    @NonNull
    private String presentationId;
    @NonNull
    private String userId;
    private Timestamp createdAt = Timestamp.now();
    private Timestamp updatedAt = Timestamp.now();
}
