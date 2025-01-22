package com.viniciuscastro.elements.controllers;

import com.viniciuscastro.elements.services.WordCloudService;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

@Authenticated
@Path("element/word-cloud")
public class WordCloudController {
    @Inject
    WordCloudService wordCloudService;    
}
