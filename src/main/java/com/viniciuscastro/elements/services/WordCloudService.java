package com.viniciuscastro.elements.services;

import com.viniciuscastro.elements.repositories.WordCloudRepository;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class WordCloudService {
    @Inject
    WordCloudRepository repository;    
}
