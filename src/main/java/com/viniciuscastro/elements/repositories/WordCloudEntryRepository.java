package com.viniciuscastro.elements.repositories;

import java.util.List;

import com.viniciuscastro.elements.models.WordCloud;
import com.viniciuscastro.elements.models.WordCloudEntry;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class WordCloudEntryRepository implements PanacheRepository<WordCloudEntry> {
    public WordCloudEntry findById(String id) {
        WordCloudEntry wordEntry = this.find("id", id)
            .firstResult();
        return wordEntry;
    }

    public List<WordCloudEntry> findByEntry(String entry) {
        List<WordCloudEntry> wordEntries = this.find("entry", entry)
            .list();
        return wordEntries;
    }

    public List<WordCloudEntry> findByWordCloudId(WordCloud wordCloud) {
        List<WordCloudEntry> wordEntries = this.find("wordCloud", wordCloud)
            .list();
        return wordEntries;
    }
}
