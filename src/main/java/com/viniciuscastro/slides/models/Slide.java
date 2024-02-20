package com.viniciuscastro.slides.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class Slide {
    private String presentationId;
    private String title;
    private List<SlidePage> slides;
}
