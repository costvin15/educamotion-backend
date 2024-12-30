package com.viniciuscastro.elements.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateQuestionRequest {
    private String id;
    private String question;
    private String description;
    private String type;
    private String[] options;
    private String correctOption;
}
