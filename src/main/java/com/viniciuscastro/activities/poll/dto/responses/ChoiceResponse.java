package com.viniciuscastro.activities.poll.dto.responses;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChoiceResponse {
    private String id;
    private String pollId;
    private String description;
    private Date createdAt;
    private Date updatedAt;
}
