package com.viniciuscastro.elements.services;

import java.util.List;

import com.viniciuscastro.dto.response.ElementResponse;
import com.viniciuscastro.elements.dto.response.QuestionResponse;
import com.viniciuscastro.elements.models.Question;
import com.viniciuscastro.elements.models.QuestionType;
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
            question.getDescription(),
            question.getType().name(),
            question.getOptions().toArray(new String[0]),
            question.getCorrectOption()
        );
    }

    @Transactional
    public QuestionResponse createQuestionElement(String elementId, String title, String description, String type, String[] options, String correctOption) {
        ElementResponse element = this.elementService.getElement(elementId);
        if (element == null) {
            throw new IllegalArgumentException("Element not found");
        }

        QuestionType questionType = QuestionType.valueOf(type);
        List<String> optionsList = List.of(options);

        if (questionType == QuestionType.DISCURSIVE) {
            optionsList = List.of();
            correctOption = null;
        }

        Question question = Question.builder()
            .id(elementId)
            .title(title)
            .description(description)
            .type(questionType)
            .options(optionsList)
            .correctOption(correctOption)
            .build();
        this.questionRepository.persist(question);

        return this.getQuestionElement(elementId);
    }

    @Transactional
    public QuestionResponse updateQuestionElement(String questionId, String title, String description, String type, String[] options, String correctOption) {
        Question question = this.questionRepository.findByElementId(questionId);
        if (question == null) {
            throw new IllegalArgumentException("Question not found");
        }

        QuestionType questionType = QuestionType.valueOf(type);
        List<String> optionsList = List.of(options);

        if (questionType == QuestionType.DISCURSIVE) {
            optionsList = List.of();
            correctOption = null;
        }

        question.setTitle(title);
        question.setDescription(description);
        question.setType(questionType);
        question.setOptions(optionsList);
        question.setCorrectOption(correctOption);

        this.questionRepository.persist(question);

        return this.getQuestionElement(questionId);
    }
}
