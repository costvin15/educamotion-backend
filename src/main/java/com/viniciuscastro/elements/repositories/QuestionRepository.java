package com.viniciuscastro.elements.repositories;

import com.viniciuscastro.elements.models.Question;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class QuestionRepository implements PanacheRepository<Question> {
    public Question findByElementId(String elementId) {
        return this.find("id", elementId)
            .firstResult();
    }
}
