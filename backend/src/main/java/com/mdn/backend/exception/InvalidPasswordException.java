package com.mdn.backend.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String s) {
        super(s);
    }
}
