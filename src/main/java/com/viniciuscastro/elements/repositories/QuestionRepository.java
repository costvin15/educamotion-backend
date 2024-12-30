package com.viniciuscastro.elements.repositories;

import com.viniciuscastro.elements.models.Question;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class QuestionRepository implements PanacheRepository<Question> {
}
