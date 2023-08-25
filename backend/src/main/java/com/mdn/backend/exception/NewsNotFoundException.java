package com.mdn.backend.exception;

public class NewsNotFoundException extends RuntimeException {
        public NewsNotFoundException(String message) {
            super(message);
        }
}
