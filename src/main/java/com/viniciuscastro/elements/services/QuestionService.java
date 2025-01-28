package com.viniciuscastro.elements.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.viniciuscastro.dto.response.ElementResponse;
import com.viniciuscastro.elements.dto.response.PartialQuestionAnswerResponse;
import com.viniciuscastro.elements.dto.response.QuestionAnswerGroupedByPage;
import com.viniciuscastro.elements.dto.response.QuestionAnswerReportResponse;
import com.viniciuscastro.elements.dto.response.QuestionAnswerResponse;
import com.viniciuscastro.elements.dto.response.QuestionAnswersInformation;
import com.viniciuscastro.elements.dto.response.AnonimousQuestionAnswerResponse;
import com.viniciuscastro.elements.dto.response.QuestionResponse;
import com.viniciuscastro.elements.models.Question;
import com.viniciuscastro.elements.models.QuestionAnswer;
import com.viniciuscastro.elements.models.QuestionType;
import com.viniciuscastro.elements.repositories.QuestionAnswerRepository;
import com.viniciuscastro.elements.repositories.QuestionRepository;
import com.viniciuscastro.models.presentations.Element;
import com.viniciuscastro.models.presentations.ElementType;
import com.viniciuscastro.repositories.ElementRepository;
import com.viniciuscastro.services.ElementService;
import com.viniciuscastro.services.UserService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class QuestionService {
    @Inject
    ElementService elementService;

    @Inject
    UserService userService;

    @Inject
    QuestionRepository questionRepository;

    @Inject
    ElementRepository elementRepository;

    @Inject
    QuestionAnswerRepository questionAnswerRepository;

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

    public PartialQuestionAnswerResponse getQuestionAnswer(String elementId) {
        Question question = this.questionRepository.findByElementId(elementId);
        if (question == null) {
            throw new IllegalArgumentException("Question not found");
        }

        QuestionAnswer questionAnswer = this.questionAnswerRepository.findByQuestionIdAndUserId(elementId, this.userService.getUserId());
        if (questionAnswer == null) {
            throw new IllegalArgumentException("Question not answered");
        }

        return new PartialQuestionAnswerResponse(
            questionAnswer.getQuestion().getId(),
            questionAnswer.getAnswer(),
            questionAnswer.getAnsweredAt()
        );
    }

    @Transactional
    public AnonimousQuestionAnswerResponse answerQuestion(String elementId, String answer) {
        Question question = this.questionRepository.findByElementId(elementId);
        if (question == null) {
            throw new IllegalArgumentException("Question not found");
        }

        QuestionAnswer questionAnswer = this.questionAnswerRepository.findByQuestionIdAndUserId(elementId, this.userService.getUserId());
        if (questionAnswer == null) {
            questionAnswer = new QuestionAnswer();
        }

        String correctOption = question.getCorrectOption();
        boolean correct = correctOption == null || correctOption.equals(answer);

        questionAnswer.setQuestion(question);
        questionAnswer.setUserId(this.userService.getUserId());
        questionAnswer.setAnswer(answer);
        questionAnswer.setCorrect(correct);
        questionAnswer.setAnsweredAt(Date.from(Instant.now()));

        this.questionAnswerRepository.persist(questionAnswer);

        return new AnonimousQuestionAnswerResponse(
            questionAnswer.getQuestion().getId(),
            questionAnswer.getAnswer(),
            questionAnswer.isCorrect(),
            questionAnswer.getAnsweredAt()
        );
    }

    public QuestionAnswerReportResponse listAnswersGroupedByPage(String presentationId) {
        QuestionAnswerReportResponse response = new QuestionAnswerReportResponse();
        Map<String, QuestionAnswerGroupedByPage> groups = new HashMap<>();
        List<Element> elements = this.elementRepository.findByPresentationIdAndTypeAndUser(presentationId, this.userService.getUserId(), ElementType.QUESTION);

        for (Element element : elements) {
            Question question = this.questionRepository.findByElementId(element.getId());

            if (question == null) {
                throw new IllegalArgumentException("Question not found");
            }

            if (groups.get(element.getSlideId()) == null) {
                groups.put(element.getSlideId(), new QuestionAnswerGroupedByPage());
                groups.get(element.getSlideId()).setAnswers(new ArrayList<>());
            }

            groups.get(element.getSlideId()).setPage(element.getSlideId());

            List<QuestionAnswer> answers = this.questionAnswerRepository.findByQuestionId(element.getId());
            for (QuestionAnswer answer : answers) {
                groups.get(element.getSlideId()).getAnswers().add(new QuestionAnswerResponse(
                    answer.getQuestion().getId(),
                    answer.getUserId(),
                    answer.getAnswer(),
                    answer.isCorrect(),
                    answer.getAnsweredAt()
                ));
            }
        }

        response.setPages(new ArrayList<>());

        for (QuestionAnswerGroupedByPage group : groups.values()) {
            response.getPages().add(group);
        }

        return response;
    }

    public QuestionAnswersInformation getQuestionAnswersInformation(String questionId) {
        Question question = this.questionRepository.findByElementId(questionId);
        if (question == null) {
            throw new IllegalArgumentException("Question not found");
        }

        long totalAnswers = this.countAnswers(questionId);
        Map<String, Long> answersByOption = this.countAnswersByOption(questionId);

        return new QuestionAnswersInformation(
            totalAnswers,
            answersByOption
        );
    }

    private long countAnswers(String questionId) {
        return this.questionAnswerRepository.countByQuestionId(questionId);
    }

    private Map<String, Long> countAnswersByOption(String questionId) {
        return this.questionAnswerRepository.groupAnswersByOption(questionId);
    }
}
