package com.viniciuscastro.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {
    private String message;
    private StatusCode statusCode;

    @Getter
    @AllArgsConstructor
    public enum StatusCode {
        INTERNAL_SERVER_ERROR(500),
        NOT_FOUND(404),
        BAD_REQUEST(400),
        FORBIDDEN(403);

        private int code;
    }
}
