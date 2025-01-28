package com.viniciuscastro.elements.dto.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionAnswersInformation {
    private long totalAnswers;
    private Map<String, Long> answersGroupedByOption;
}
