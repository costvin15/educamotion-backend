package com.viniciuscastro.elements.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateWordCloudEntryRequest {
    public String wordCloudId;
    public String entry;    
}
