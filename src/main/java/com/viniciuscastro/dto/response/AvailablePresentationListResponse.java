package com.viniciuscastro.dto.response;

import java.util.List;

import lombok.Getter;

@Getter
public class AvailablePresentationListResponse {
    private int total;
    private List<PresentationResponse> presentations;
    private String nextPageToken;

    public AvailablePresentationListResponse(List<PresentationResponse> presentations, String nextPageToken) {
        this.presentations = presentations;
        this.total = presentations.size();
        this.nextPageToken = nextPageToken;
    }
}
