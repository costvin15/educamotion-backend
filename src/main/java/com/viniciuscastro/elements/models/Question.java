package com.viniciuscastro.elements.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "question_element")
public class Question {
    @Id
    private String id;
    private String title;
    private String description;
}
