package com.viniciuscastro.activities.freeanswer.dto.responses;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FreeAnswerResponse {
    private String id;
    private String presentationId;
    private String question;
    private Integer maxWords;
    private Date createdAt;
    private Date updatedAt;
}
