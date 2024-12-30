package com.viniciuscastro.dto.response;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresentationDetailResponse {
    private String id;
    private String title;
    private URL thumbnail;
    private Date lastModified;
    private List<String> slidesIds;
    private Map<String, ElementResponse> elements;
}
