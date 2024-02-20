package com.viniciuscastro.slides.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class Slide {
    private String presentationId;
    private String title;
    private List<SlidePage> slides;
}
