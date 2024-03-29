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
        NO_CONTENT(204),
        BAD_REQUEST(400),
        FORBIDDEN(403),
        NOT_FOUND(404),
        INTERNAL_SERVER_ERROR(500);

        private int code;
    }
}
