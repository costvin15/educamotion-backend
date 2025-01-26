package com.viniciuscastro.elements.services;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.viniciuscastro.dto.response.ElementResponse;
import com.viniciuscastro.elements.dto.response.FrequencyDistributionItem;
import com.viniciuscastro.elements.dto.response.FrequencyDistributionResponse;
import com.viniciuscastro.elements.dto.response.WordCloudEntryResponse;
import com.viniciuscastro.elements.dto.response.WordCloudResponse;
import com.viniciuscastro.elements.models.WordCloud;
import com.viniciuscastro.elements.models.WordCloudEntry;
import com.viniciuscastro.elements.repositories.WordCloudEntryRepository;
import com.viniciuscastro.elements.repositories.WordCloudRepository;
import com.viniciuscastro.services.ElementService;
import com.viniciuscastro.services.UserService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class WordCloudService {
    @Inject
    ElementService elementService;

    @Inject
    UserService userService;

    @Inject
    WordCloudRepository wordCloudRepository;

    @Inject
    WordCloudEntryRepository wordCloudEntryRepository;

    public WordCloudResponse getWordCloudElement(String elementId) {
        ElementResponse element = this.elementService.getElement(elementId);

        WordCloud wordCloud = this.wordCloudRepository.findByElementId(element.getId());
        if (wordCloud == null) {
            throw new IllegalArgumentException("WordCloud not found");
        }

        List<WordCloudEntry> entries = this.wordCloudEntryRepository.findByWordCloudId(wordCloud);
        List<WordCloudEntryResponse> entryResponses = entries.stream()
            .map(entry -> new WordCloudEntryResponse(
                entry.getId(),
                entry.getWordCloud().getId(),
                entry.getUserId(),
                entry.getEntry(),
                entry.getCreatedAt().toString()
            ))
            .toList();

        return new WordCloudResponse(
            wordCloud.getId(),
            wordCloud.getTitle(),
            wordCloud.getEnableMultipleEntries(),
            entryResponses
        );
    }

    public WordCloudEntryResponse getWordCloudEntry(String entryId) {
        WordCloudEntry wordEntry = this.wordCloudEntryRepository.findById(entryId);
        if (wordEntry == null) {
            throw new IllegalArgumentException("WordCloudEntry not found");
        }

        return new WordCloudEntryResponse(
            wordEntry.getId(),
            wordEntry.getWordCloud().getId(),
            wordEntry.getUserId(),
            wordEntry.getEntry(),
            wordEntry.getCreatedAt().toString()
        );
    }

    @Transactional
    public WordCloudResponse createWordCloudElement(String elementId, String title, Boolean enableMultipleEntries) {
        ElementResponse element = this.elementService.getElement(elementId);

        WordCloud wordCloud = WordCloud.builder()
            .id(element.getId())
            .title(title)
            .enableMultipleEntries(enableMultipleEntries)
            .build();
        this.wordCloudRepository.persist(wordCloud);
        return this.getWordCloudElement(elementId);
    }

    public WordCloudResponse updateWordCloudElement(String elementId, String title, Boolean enableMultipleEntries) {
        ElementResponse element = this.elementService.getElement(elementId);

        WordCloud wordCloud = this.wordCloudRepository.findByElementId(element.getId());
        if (wordCloud == null) {
            throw new IllegalArgumentException("WordCloud not found");
        }

        wordCloud.setTitle(title);
        wordCloud.setEnableMultipleEntries(enableMultipleEntries);
        this.wordCloudRepository.persist(wordCloud);

        return this.getWordCloudElement(elementId);
    }

    @Transactional
    public WordCloudEntryResponse addWordCloudEntry(String elementId, String entry) {
        ElementResponse element = this.elementService.getElement(elementId);

        WordCloud wordCloud = this.wordCloudRepository.findByElementId(element.getId());
        if (wordCloud == null) {
            throw new IllegalArgumentException("WordCloud not found");
        }

        List<WordCloudEntry> existingEntry = this.wordCloudEntryRepository.findByEntry(entry);
        if (!wordCloud.getEnableMultipleEntries() && !existingEntry.isEmpty()) {
            throw new IllegalArgumentException("Entry already exists");
        }

        WordCloudEntry wordEntry = WordCloudEntry.builder()
            .id(UUID.randomUUID().toString())
            .wordCloud(wordCloud)
            .userId(this.userService.getUserId())
            .entry(entry)
            .createdAt(Date.from(Instant.now()))
            .build();
        this.wordCloudEntryRepository.persist(wordEntry);

        return this.getWordCloudEntry(wordEntry.getId());
    }

    public FrequencyDistributionResponse getFrequencyDistribution(String elementId) {
        ElementResponse element = this.elementService.getElement(elementId);

        WordCloud wordCloud = this.wordCloudRepository.findByElementId(element.getId());
        if (wordCloud == null) {
            throw new IllegalArgumentException("WordCloud not found");
        }

        List<WordCloudEntry> entries = this.wordCloudEntryRepository.findByWordCloudId(wordCloud);
        List<String> words = entries.stream()
            .map(WordCloudEntry::getEntry)
            .toList();

        return new FrequencyDistributionResponse(
            words.stream()
                .distinct()
                .map(word -> new FrequencyDistributionItem(
                    word,
                    (int) words.stream().filter(w -> w.equals(word)).count()
                ))
                .toList()
        );
    }
}
