package com.viniciuscastro.poll.clients.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PollRequest {
    private String question;
    private String presentationId;
}
