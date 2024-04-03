package com.viniciuscastro.activity.dto.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActivityListResponse {
    private int total;
    private List<ActivityResponse> activities;
}
