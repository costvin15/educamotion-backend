package com.viniciuscastro.elements.controllers;

import com.viniciuscastro.elements.dto.request.CreateWordCloudRequest;
import com.viniciuscastro.elements.dto.response.WordCloudResponse;
import com.viniciuscastro.elements.services.WordCloudService;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Authenticated
@Path("element/word-cloud")
public class WordCloudController {
    @Inject
    WordCloudService wordCloudService;

    @GET
    @Path("detail/{elementId}")
    public WordCloudResponse getWordCloud(String elementId) {
        return this.wordCloudService.getWordCloudElement(elementId);
    }

    @POST
    @Path("add")
    public WordCloudResponse addQuestion(CreateWordCloudRequest request) {
        return this.wordCloudService.createWordCloudElement(
            request.getId(),
            request.getTitle(),
            request.getEnableMultipleEntries()
        );
    }
}
