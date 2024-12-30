package com.viniciuscastro.elements.services;

import com.viniciuscastro.dto.response.ElementResponse;
import com.viniciuscastro.elements.dto.response.QuestionResponse;
import com.viniciuscastro.elements.models.Question;
import com.viniciuscastro.elements.repositories.QuestionRepository;
import com.viniciuscastro.services.ElementService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class QuestionService {
    @Inject
    ElementService elementService;

    @Inject
    QuestionRepository questionRepository;

    public QuestionResponse getQuestionElement(String elementId) {
        ElementResponse element = this.elementService.getElement(elementId);
        if (element == null) {
            throw new IllegalArgumentException("Element not found");
        }

        Question question = this.questionRepository.findByElementId(elementId);
        if (question == null) {
            throw new IllegalArgumentException("Question not found");
        }

        return new QuestionResponse(
            question.getId(),
            question.getTitle(),
            question.getDescription()
        );
    }

    @Transactional
    public QuestionResponse createQuestionElement(String elementId, String title, String description) {
        ElementResponse element = this.elementService.getElement(elementId);
        if (element == null) {
            throw new IllegalArgumentException("Element not found");
        }

        Question question = Question.builder()
            .id(elementId)
            .title(title)
            .description(description)
            .build();
        this.questionRepository.persist(question);

        return this.getQuestionElement(elementId);
    }
}
