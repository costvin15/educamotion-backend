package com.viniciuscastro.elements.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionAnswerRequest {
    private String id;
    private String answer;
}
