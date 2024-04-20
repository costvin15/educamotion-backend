package com.viniciuscastro.activities.poll.dto.responses;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PollResponse {
    private String id;
    private String presentationId;
    private String question;
    private List<ChoiceResponse> choices;
    private Date createdAt;
    private Date updatedAt;
}
