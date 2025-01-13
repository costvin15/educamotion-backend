package com.viniciuscastro.elements.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PartialQuestionAnswerResponse {
    private String questionId;
    private String answer;
    private Date answeredAt;
}
