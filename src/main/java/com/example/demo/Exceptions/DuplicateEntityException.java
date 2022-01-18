package com.example.demo.Exceptions;

public class DuplicateEntityException extends RuntimeException{
    public DuplicateEntityException() {
    }

    public DuplicateEntityException(String message) {
        super(message);
    }

    public DuplicateEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
