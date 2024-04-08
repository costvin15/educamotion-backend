package com.viniciuscastro.session.dto.responses;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdmittanceResponse {
    private String id;
    private String userId;
    private String sessionId;
    private Date createdAt;
}
