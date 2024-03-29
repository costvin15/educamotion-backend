package com.viniciuscastro.clients.models;

import java.util.List;

import com.viniciuscastro.presentation.models.PresentationPage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GooglePresentation {
    private String presentationId;
    private String title;
    private List<PresentationPage> slides;
    private String revisionId;
}
