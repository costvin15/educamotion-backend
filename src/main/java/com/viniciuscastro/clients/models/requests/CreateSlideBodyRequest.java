package com.viniciuscastro.clients.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

enum Type {
    NONE,
    BODY,
    CHART,
    CLIP_ART,
    CENTERED_TITLE,
    DIAGRAM,
    DATE_AND_TIME,
    FOOTER,
    HEADER,
    MEDIA,
    OBJECT,
    PICTURE,
    SLIDE_NUMBER,
    SUBTITLE,
    TABLE,
    TITLE,
    SLIDE_IMAGE;
}

@Builder
@AllArgsConstructor
@Getter
class Placeholder {
    private Type type;
    private Integer index;
    private String parentObjectId;
}

@Builder
@AllArgsConstructor
@Getter
class LayoutPlaceholderIdMapping {
    private String objectId;
    private Placeholder layoutPlaceholder;
    private String layoutPlaceholderObjectId;
}

@Builder
@AllArgsConstructor
@Getter
public class CreateSlideBodyRequest {
    private String objectId;
    private Integer insertionIndex;
    private SlideLayoutReference slideLayoutReference;
    private LayoutPlaceholderIdMapping[] placeholderIdMappings;
}
