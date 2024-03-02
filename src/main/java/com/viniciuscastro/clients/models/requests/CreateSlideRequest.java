package com.viniciuscastro.clients.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

enum PredefinedLayout {
    PREDEFINED_LAYOUT_UNSPECIFIED,
    BLANK,
    CAPTION_ONLY,
    TITLE,
    TITLE_AND_BODY,
    TITLE_AND_TWO_COLUMNS,
    TITLE_ONLY,
    SECTION_HEADER,
    SECTION_TITLE_AND_DESCRIPTION,
    ONE_COLUMN_TEXT,
    MAIN_POINT,
    BIG_NUMBER;
}

@Builder
@AllArgsConstructor
@Getter
class LayoutReference {
    private PredefinedLayout predefinedLayout;
    private String layoutId;
}

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
public class CreateSlideRequest {
    private String objectId;
    private Integer insertionIndex;
    private LayoutReference slideLayoutReference;
    private LayoutPlaceholderIdMapping[] placeholderIdMappings;
}
