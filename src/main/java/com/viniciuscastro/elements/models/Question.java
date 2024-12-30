package com.viniciuscastro.elements.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "question_element")
public class Question {
    @Id
    private String id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private QuestionType type;
    private List<String> options;
    @Column(name = "correct_option")
    private String correctOption;
}
