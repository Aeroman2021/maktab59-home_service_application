package com.example.demo.Exceptions;


public class CreditCardException extends RuntimeException {
    public CreditCardException() {
    }

    public CreditCardException(String message) {
        super(message);
    }

    public CreditCardException(String message, Throwable cause) {
        super(message, cause);
    }
}
