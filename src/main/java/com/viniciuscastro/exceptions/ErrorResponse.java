package com.viniciuscastro.exceptions;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private UUID errorId;
    private String errorMessage;
}
