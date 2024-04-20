package com.viniciuscastro.activities.freeanswer.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FreeAnswerRequest {
    private String question;
    private Integer maxWords;
    private String presentationId;
}
