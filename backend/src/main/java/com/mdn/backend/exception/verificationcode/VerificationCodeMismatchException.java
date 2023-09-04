package com.mdn.backend.exception.verificationcode;

public class VerificationCodeMismatchException extends RuntimeException {
    public VerificationCodeMismatchException(String s) {
        super(s);
    }
}
