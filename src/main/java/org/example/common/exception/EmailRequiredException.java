package org.example.common.exception;

public class EmailRequiredException extends RuntimeException {
    public EmailRequiredException(String message) {
        super(message);
    }
}
