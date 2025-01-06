package com.viniciuscastro.dto.response;

import java.util.List;

import lombok.Getter;

@Getter
public class UserAttendanceListResponse {
    private int total;
    private List<UserAttendanceResponse> attendances;

    public UserAttendanceListResponse(List<UserAttendanceResponse> attendances) {
        this.total = attendances.size();
        this.attendances = attendances;
    }
}
