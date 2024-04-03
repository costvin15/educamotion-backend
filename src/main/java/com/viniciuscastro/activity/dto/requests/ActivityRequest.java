package com.viniciuscastro.activity.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRequest {
    private String presentationId;
    private String activityId;
    private String type;
}
