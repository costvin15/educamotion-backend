package com.viniciuscastro.clients.models.requests;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BatchUpdateRequest {
    private CreateSlideBodyRequest createSlide;
    private UpdateSlidesPositionRequest updateSlidesPosition;
    private UpdatePagePropertiesRequest updatePageProperties;
}
