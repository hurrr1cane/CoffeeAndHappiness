package com.mdn.backend.exception.notfound;

public class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException(String message) {
            super(message);
        }
}
