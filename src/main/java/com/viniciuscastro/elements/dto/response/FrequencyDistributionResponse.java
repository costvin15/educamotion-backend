package com.viniciuscastro.elements.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FrequencyDistributionResponse {
    private List<FrequencyDistributionItem> frequencyDistribution;
}
