package com.mdn.backend.exception.verificationcode;

public class VerificationCodeNotFoundException extends RuntimeException {
    public VerificationCodeNotFoundException(String s) {
        super(s);
    }
}
