package com.viniciuscastro.activities.poll.dto.responses;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StorePollResponse {
    private String id;
    private String presentationId;
    private String question;
    private String[] choices;
    private Date createdAt;
    private Date updatedAt;
}
