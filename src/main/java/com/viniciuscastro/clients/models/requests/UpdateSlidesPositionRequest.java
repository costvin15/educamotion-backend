package com.viniciuscastro.clients.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UpdateSlidesPositionRequest {
    private String[] slideObjectIds;
    private Integer insertionIndex;
}
