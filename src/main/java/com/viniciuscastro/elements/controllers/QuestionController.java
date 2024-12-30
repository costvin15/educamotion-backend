package com.viniciuscastro.elements.controllers;

import com.viniciuscastro.elements.dto.request.CreateQuestionRequest;
import com.viniciuscastro.elements.dto.response.QuestionResponse;
import com.viniciuscastro.elements.services.QuestionService;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Authenticated
@Path("element/question")
public class QuestionController {
    @Inject
    QuestionService questionService;

    @GET
    @Path("detail/{elementId}")
    public QuestionResponse getQuestion(String elementId) {
        return this.questionService.getQuestionElement(elementId);
    }

    @POST
    @Path("add")
    public QuestionResponse addQuestion(CreateQuestionRequest request) {
        return this.questionService.createQuestionElement(
            request.getId(),
            request.getQuestion(),
            request.getDescription()
        );
    }
}
