package com.viniciuscastro.clients.models;

import com.google.cloud.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class GooglePresentationSearchResult {
    @NonNull
    private String presentationId;
    private Timestamp createdAt = Timestamp.now();
    private Timestamp updatedAt = Timestamp.now();
    private boolean exists = false;
}
