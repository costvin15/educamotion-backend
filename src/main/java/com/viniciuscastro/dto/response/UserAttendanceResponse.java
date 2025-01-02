package com.viniciuscastro.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAttendanceResponse {
    private String id;
    private String name;
    private String profilePicture;
}
