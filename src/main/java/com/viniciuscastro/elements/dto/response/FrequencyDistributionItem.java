package com.viniciuscastro.elements.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FrequencyDistributionItem {
    private String word;
    private Integer frequency;
}
