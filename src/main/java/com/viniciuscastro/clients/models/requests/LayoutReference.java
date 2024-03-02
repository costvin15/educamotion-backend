package com.viniciuscastro.clients.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class LayoutReference {
    private PredefinedLayout predefinedLayout;
    private String layoutId;

    public enum PredefinedLayout {
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
}
