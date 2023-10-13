package com.mdn.backend.exception.notfound;

public class FoodNotFoundException extends RuntimeException {
    public FoodNotFoundException(String s) {
        super(s);
    }
}
