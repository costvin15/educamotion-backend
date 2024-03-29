package com.viniciuscastro.presentation.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresentationListResponse {
    private int total;
    private List<PresentationResponse> presentations;
}
