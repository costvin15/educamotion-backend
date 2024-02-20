package com.viniciuscastro.slides.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class SlideThumbnail {
    private Integer width;
    private Integer height;
    private String contentUrl;
}
