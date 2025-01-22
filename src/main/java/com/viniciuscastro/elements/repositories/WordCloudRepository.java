package com.viniciuscastro.elements.repositories;

import com.viniciuscastro.elements.models.WordCloud;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class WordCloudRepository implements PanacheRepository<WordCloud> {    
}
