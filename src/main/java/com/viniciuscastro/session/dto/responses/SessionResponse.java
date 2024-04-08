package com.viniciuscastro.session.dto.responses;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionResponse {
    private String code;
    private String presentationId;
    private boolean active;
    private Date createdAt;
    private Date updatedAt;
}
