package com.viniciuscastro.slides.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class SlideThumbnail {
    private Integer width;
    private Integer height;
    private String contentUrl;
}
