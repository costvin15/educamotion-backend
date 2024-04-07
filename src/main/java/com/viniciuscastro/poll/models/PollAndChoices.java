package com.viniciuscastro.poll.models;

import java.util.List;

import com.google.cloud.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PollAndChoices {
    private String id;
    private String question;
    private String presentationId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<Choice> choices;
}
