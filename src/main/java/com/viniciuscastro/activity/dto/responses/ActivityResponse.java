package com.viniciuscastro.activity.dto.responses;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponse {
    private String id;
    private String presentationId;
    private String activityId;
    private String activityType;
    private String objectId;
    private Date createdAt;
    private Date updatedAt;
}
