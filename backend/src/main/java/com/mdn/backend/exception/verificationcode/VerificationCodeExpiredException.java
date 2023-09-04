package com.mdn.backend.exception.verificationcode;

public class VerificationCodeExpiredException extends RuntimeException {
    public VerificationCodeExpiredException(String s) {
        super(s);
    }
}
