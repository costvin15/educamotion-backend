package com.viniciuscastro.clients.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ThumbnailFirestore {
    private String presentationId;
    private String objectId;
}
