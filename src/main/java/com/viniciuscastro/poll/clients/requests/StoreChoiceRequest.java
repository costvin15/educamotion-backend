package com.viniciuscastro.poll.clients.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreChoiceRequest {
    private String description;
    private String pollId;
}
