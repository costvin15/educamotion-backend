package com.viniciuscastro.activities.poll.clients.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StorePollRequest {
    private String question;
    private String presentationId;
}
