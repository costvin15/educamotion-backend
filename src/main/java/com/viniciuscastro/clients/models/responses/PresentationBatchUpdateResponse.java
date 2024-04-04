package com.viniciuscastro.clients.models.responses;

import com.viniciuscastro.clients.models.WriteControlBody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class PresentationBatchUpdateResponse {
    private String presentationId;
    private UpdatePresentationResponse[] replies;
    private WriteControlBody writeControl;
}
