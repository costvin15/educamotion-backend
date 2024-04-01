package com.viniciuscastro.poll.clients.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChoiceRequest {
    private String description;
    private String pollId;
}
