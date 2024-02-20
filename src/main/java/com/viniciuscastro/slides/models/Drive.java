package com.viniciuscastro.slides.models;

import com.viniciuscastro.slides.resources.MimeType;

import jakarta.ws.rs.QueryParam;
import lombok.Getter;

public class Drive {
    @QueryParam("q")
    private String q;

    @Getter
    @QueryParam("pageToken")
    private String pageToken;

    public Drive(MimeType mimeType, String pageToken) {
        this.q = mimeType.mimeType;
        this.pageToken = pageToken;
    }
}
