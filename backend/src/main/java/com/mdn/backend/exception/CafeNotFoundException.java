package com.mdn.backend.exception;

public class CafeNotFoundException extends RuntimeException {
    public CafeNotFoundException(String s) {
        super(s);
    }
}
