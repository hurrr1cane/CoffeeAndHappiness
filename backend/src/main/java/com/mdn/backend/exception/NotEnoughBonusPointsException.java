package com.mdn.backend.exception;

public class NotEnoughBonusPointsException extends RuntimeException {
    public NotEnoughBonusPointsException(String message) {
        super(message);
    }
}
