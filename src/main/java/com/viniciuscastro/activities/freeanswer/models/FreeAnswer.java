package com.viniciuscastro.activities.freeanswer.models;

import com.google.cloud.Timestamp;
import com.viniciuscastro.activities.models.BaseActivity;

import lombok.Getter;

@Getter
public class FreeAnswer extends BaseActivity {
    private String question;
    private Long maxWords;
    
    public FreeAnswer(String presentationId, String question, Long maxWords) {
        super(presentationId);

        this.question = question;
        this.maxWords = maxWords;
    }

    public FreeAnswer(String id, String presentationId, String question, Long maxWords, Timestamp createdAt, Timestamp updatedAt) {
        super(id, presentationId, createdAt, updatedAt);

        this.question = question;
        this.maxWords = maxWords;
    }
}
