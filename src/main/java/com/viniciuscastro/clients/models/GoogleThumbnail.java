package com.viniciuscastro.clients.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class GoogleThumbnail {
    private Integer width;
    private Integer height;
    private String contentUrl;
}
