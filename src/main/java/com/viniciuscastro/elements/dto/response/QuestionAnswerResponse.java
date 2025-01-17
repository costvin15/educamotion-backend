package com.viniciuscastro.elements.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerResponse {
    private String questionId;
    private String userId;
    private String answer;
    private boolean correct;
    private Date answeredAt;
}
