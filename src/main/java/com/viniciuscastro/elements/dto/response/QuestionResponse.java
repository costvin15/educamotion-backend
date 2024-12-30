package com.viniciuscastro.elements.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionResponse {
    private String id;
    private String title;
    private String description;
    private String type;
    private String[] options;
    private String correctOption;
}
