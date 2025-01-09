package com.viniciuscastro.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleDriveSearchResponse {
    private String nextPageToken;
    private List<GoogleDriveFileResponse> files;
}
