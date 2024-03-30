package com.viniciuscastro.presentation.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresentationResponse {
    private String presentationId;
    private String title;
    private Date createdAt;
    private Date updatedAt;
}
