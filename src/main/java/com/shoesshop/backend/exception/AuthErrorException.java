package com.shoesshop.backend.exception;

public class AuthErrorException extends RuntimeException {
    public AuthErrorException(String message) {
        super(message);
    }

    public AuthErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
