package com.viniciuscastro.poll.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PollRequest {
    private String question;
    private String presentationId;
    private String[] choices;
}
