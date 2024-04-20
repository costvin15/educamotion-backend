package com.viniciuscastro.activities.freeanswer.clients.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreFreeAnswerRequest {
    private String question;
    private Integer maxWords;
    private String presentationId;
}
