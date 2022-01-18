package com.example.demo.Exceptions;

public class SuggestionException extends RuntimeException{
    public SuggestionException() {
    }

    public SuggestionException(String message) {
        super(message);
    }

    public SuggestionException(String message, Throwable cause) {
        super(message, cause);
    }
}
