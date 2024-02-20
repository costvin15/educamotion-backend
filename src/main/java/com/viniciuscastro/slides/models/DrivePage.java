package com.viniciuscastro.slides.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class DrivePage {
    private String nextPageToken;
    private List<DriveFile> files;
}
