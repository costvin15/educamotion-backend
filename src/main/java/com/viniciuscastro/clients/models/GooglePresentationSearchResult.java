package com.viniciuscastro.clients.models;

import com.google.cloud.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class GooglePresentationSearchResult {
    @NonNull
    private String presentationId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean exists;
}