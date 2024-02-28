package com.viniciuscastro.presentation.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Presentation {
    private String presentationId;
    private String title;
    private List<PresentationPage> slides;
}
