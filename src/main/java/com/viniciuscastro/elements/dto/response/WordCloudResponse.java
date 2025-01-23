package com.viniciuscastro.elements.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WordCloudResponse {
    private String id;
    private String title;
    private Boolean enableMultipleEntries;    
}
