package com.shoesshop.backend.exception;

public class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateEntryException(String message) {
        super(message);
    }
}
