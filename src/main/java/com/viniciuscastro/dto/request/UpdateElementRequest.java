package com.viniciuscastro.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateElementRequest {
    private String elementId;
    private int positionX;
    private int positionY;
    private int width;
    private int height;
}
