package com.mdn.backend.exception.notfound;

public class NewsNotFoundException extends RuntimeException {
        public NewsNotFoundException(String message) {
            super(message);
        }
}
