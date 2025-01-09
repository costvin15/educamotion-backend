package com.viniciuscastro.dto.response;

import java.net.URL;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresentationResponse {
    private String id;
    private String title;
    private URL thumbnail;
    private Date lastModified;

    public PresentationResponse(String id, String title) {
        this.id = id;
        this.title = title;
    }
}
