package com.viniciuscastro.elements.models;

import java.util.Date;

import com.google.auto.value.AutoValue.Builder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "question_answer")
public class QuestionAnswer {
    @Id
    @ManyToOne
    private Question question;
    @Id
    @Column(name = "user_id")
    private String userId;
    private String answer;
    private boolean correct;
    @Column(name = "answered_at")
    private Date answeredAt;
}
