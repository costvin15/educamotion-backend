package com.viniciuscastro.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ElementResponse {
    private String id;
    private String slideId;
    private String elementType;
    private Integer positionX;
    private Integer positionY;
    private Integer width;
    private Integer height;
    private boolean isOwner;
}
