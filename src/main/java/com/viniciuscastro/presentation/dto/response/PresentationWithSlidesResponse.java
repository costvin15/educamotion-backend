package com.viniciuscastro.presentation.dto.response;

import java.util.List;

import com.viniciuscastro.presentation.models.PresentationPage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresentationWithSlidesResponse {
    private String presentationId;
    private String title;
    private int totalSlides;
    private List<PresentationPage> slides;
}
