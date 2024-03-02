package com.viniciuscastro.clients.models;

import com.viniciuscastro.clients.models.responses.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class PresentationUpdateResponse {
    private String presentationId;
    private Response[] replies;
    private WriteControl writeControl;
}
