package com.viniciuscastro.clients.models.requests;

import com.viniciuscastro.clients.models.WriteControlBody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class PresentationUpdateRequest {
    BatchUpdateRequest[] requests;
    WriteControlBody writeControl;
}
