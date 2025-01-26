package com.viniciuscastro.elements.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WordCloudEntryResponse {
    private String id;
    private String wordCloudId;
    private String userId;
    private String entry;
    private String createdAt;
}
