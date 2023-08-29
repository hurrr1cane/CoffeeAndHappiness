package com.mdn.backend.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String s) {
        super(s);
    }
}
