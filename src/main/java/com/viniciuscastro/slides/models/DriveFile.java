package com.viniciuscastro.slides.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class DriveFile {
    private String id;
    private String name;
    private String kind;
    private String mimeType;
}
