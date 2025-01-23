package com.viniciuscastro.elements.services;

import com.viniciuscastro.dto.response.ElementResponse;
import com.viniciuscastro.elements.dto.response.WordCloudResponse;
import com.viniciuscastro.elements.models.WordCloud;
import com.viniciuscastro.elements.repositories.WordCloudRepository;
import com.viniciuscastro.services.ElementService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class WordCloudService {
    @Inject
    ElementService elementService;

    @Inject
    WordCloudRepository repository;

    public WordCloudResponse getWordCloudElement(String elementId) {
        ElementResponse element = this.elementService.getElement(elementId);
        if (element == null) {
            throw new IllegalArgumentException("Element not found");
        }

        WordCloud wordCloud = this.repository.findByElementId(elementId);
        if (wordCloud == null) {
            throw new IllegalArgumentException("WordCloud not found");
        }

        return new WordCloudResponse(
            wordCloud.getId(),
            wordCloud.getTitle(),
            wordCloud.getEnableMultipleEntries()
        );
    }

    @Transactional
    public WordCloudResponse createWordCloudElement(String elementId, String title, Boolean enableMultipleEntries) {
        ElementResponse element = this.elementService.getElement(elementId);
        if (element == null) {
            throw new IllegalArgumentException("Element not found");
        }

        WordCloud wordCloud = WordCloud.builder()
            .id(elementId)
            .title(title)
            .enableMultipleEntries(enableMultipleEntries)
            .build();
        this.repository.persist(wordCloud);
        return this.getWordCloudElement(elementId);
    }
}
