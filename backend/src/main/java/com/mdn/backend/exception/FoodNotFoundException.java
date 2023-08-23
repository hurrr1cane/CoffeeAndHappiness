package com.mdn.backend.exception;

public class FoodNotFoundException extends RuntimeException {
    public FoodNotFoundException(String s) {
        super(s);
    }
}
