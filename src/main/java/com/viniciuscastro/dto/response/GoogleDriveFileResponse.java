package com.viniciuscastro.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleDriveFileResponse {
    private String kind;
    private String mimeType;
    private String id;
    private String name;
}
