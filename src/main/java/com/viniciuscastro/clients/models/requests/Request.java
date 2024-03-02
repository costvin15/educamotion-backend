package com.viniciuscastro.clients.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class Request {
    private CreateSlideRequest createSlide;
}
