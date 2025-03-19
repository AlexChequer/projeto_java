package org.example.common.exception;

public class NameRequiredException extends RuntimeException {
    public NameRequiredException(String message) {
        super(message);
    }
}
