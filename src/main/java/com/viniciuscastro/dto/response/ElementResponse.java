package com.viniciuscastro.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ElementResponse {
    private String id;
    private String slideId;
    private String type;
    private Integer positionX;
    private Integer positionY;
    private Integer width;
    private Integer height;
}
