package com.viniciuscastro.presentation.models;

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
