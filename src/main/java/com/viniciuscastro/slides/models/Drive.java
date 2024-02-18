package com.viniciuscastro.slides.models;

import com.viniciuscastro.slides.resources.MimeType;

import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class Drive {
    @QueryParam("q")
    private String q;
    @QueryParam("pageToken")
    private String pageToken;

    public Drive(MimeType mimeType) {
        this.q = mimeType.mimeType;
    }

    public Drive(MimeType mimeType, String pageToken) {
        this.q = mimeType.mimeType;
        this.pageToken = pageToken;
    }
}
