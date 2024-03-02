package com.viniciuscastro.clients.models;

import com.viniciuscastro.clients.models.requests.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
class WriteControl {
    private String requiredRevisionId;
}

@Builder
@AllArgsConstructor
@Getter
public class PresentationUpdate {
    Request[] requests;
    WriteControl writeControl;
}
