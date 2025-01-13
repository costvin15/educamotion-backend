package com.viniciuscastro.elements.repositories;

import com.viniciuscastro.elements.models.QuestionAnswer;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class QuestionAnswerRepository implements PanacheRepository<QuestionAnswer> {
    public QuestionAnswer findByQuestionIdAndUserId(String questionId, String userId) {
        return this.find("question.id = ?1 and userId = ?2", questionId, userId)
            .firstResult();
    }
}
