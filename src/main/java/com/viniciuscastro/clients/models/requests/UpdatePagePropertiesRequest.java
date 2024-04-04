package com.viniciuscastro.clients.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UpdatePagePropertiesRequest {
    private String objectId;
    private String fields;
    private PageProperties pageProperties;

    @Getter
    @AllArgsConstructor
    public static class PageProperties {
        private PageBackgroundFill pageBackgroundFill;
    }

    @Getter
    @AllArgsConstructor
    public static class PageBackgroundFill {
        private StretchedPictureFill stretchedPictureFill;
    }

    @Getter
    @AllArgsConstructor
    public static class StretchedPictureFill {
        private String contentUrl;
    }
}
