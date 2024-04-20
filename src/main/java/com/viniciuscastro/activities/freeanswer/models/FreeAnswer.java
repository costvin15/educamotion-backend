package com.viniciuscastro.activities.freeanswer.models;

import com.viniciuscastro.activities.models.BaseActivity;

import lombok.Getter;

@Getter
public class FreeAnswer extends BaseActivity {
    private String question;
    private Integer maxWords;
    
    public FreeAnswer(String presentationId, String question, Integer maxWords) {
        super(presentationId);

        this.question = question;
        this.maxWords = maxWords;
    }
}
