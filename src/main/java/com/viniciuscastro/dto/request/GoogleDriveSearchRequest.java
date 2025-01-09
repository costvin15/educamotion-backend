package com.viniciuscastro.dto.request;

import jakarta.ws.rs.QueryParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoogleDriveSearchRequest {
    @QueryParam("q")
    private String q;

    @QueryParam("pageToken")
    private String pageToken;

    @QueryParam("pageSize")
    private Integer pageSize;
}
