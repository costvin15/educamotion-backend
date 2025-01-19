package com.viniciuscastro.elements.controllers;

import com.viniciuscastro.elements.dto.request.QuestionAnswerRequest;
import com.viniciuscastro.elements.dto.request.CreateQuestionRequest;
import com.viniciuscastro.elements.dto.request.UpdateQuestionRequest;
import com.viniciuscastro.elements.dto.response.PartialQuestionAnswerResponse;
import com.viniciuscastro.elements.dto.response.QuestionAnswerReportResponse;
import com.viniciuscastro.elements.dto.response.AnonimousQuestionAnswerResponse;
import com.viniciuscastro.elements.dto.response.QuestionResponse;
import com.viniciuscastro.elements.services.QuestionService;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
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
            request.getDescription(),
            request.getType(),
            request.getOptions(),
            request.getCorrectOption()
        );
    }

    @PUT
    @Path("update")
    public QuestionResponse updateQuestion(UpdateQuestionRequest request) {
        return this.questionService.updateQuestionElement(
            request.getId(),
            request.getQuestion(),
            request.getDescription(),
            request.getType(),
            request.getOptions(),
            request.getCorrectOption()
        );
    }

    @GET
    @Path("answer/{elementId}")
    public PartialQuestionAnswerResponse getAnswer(String elementId) {
        return this.questionService.getQuestionAnswer(elementId);
    }

    @POST
    @Path("answer")
    public AnonimousQuestionAnswerResponse answerQuestion(QuestionAnswerRequest request) {
        return this.questionService.answerQuestion(
            request.getId(),
            request.getAnswer()
        );
    }

    @GET
    @Path("report/{presentationId}")
    public QuestionAnswerReportResponse getReport(String presentationId) {
        return this.questionService.listAnswersGroupedByPage(presentationId);
    }
}
