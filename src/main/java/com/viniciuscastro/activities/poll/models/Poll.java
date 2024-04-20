package com.viniciuscastro.activities.poll.models;

import com.viniciuscastro.activities.models.BaseActivity;

import lombok.Getter;

@Getter
public class Poll extends BaseActivity {
    private String question;

    public Poll(String presentationId, String question) {
        super(presentationId);

        this.question = question;
    }
}
