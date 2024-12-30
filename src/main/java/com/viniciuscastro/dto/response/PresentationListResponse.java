package com.viniciuscastro.dto.response;

import java.util.List;

import lombok.Getter;

@Getter
public class PresentationListResponse {
    private int total;
    private List<PresentationResponse> presentations;

    public PresentationListResponse(List<PresentationResponse> presentations) {
        this.presentations = presentations;
        this.total = presentations.size();
    }
}
