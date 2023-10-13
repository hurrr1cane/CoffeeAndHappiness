package com.mdn.backend.exception.notfound;

public class CafeNotFoundException extends RuntimeException {
    public CafeNotFoundException(String s) {
        super(s);
    }
}
