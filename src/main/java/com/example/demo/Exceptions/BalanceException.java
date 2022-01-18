package com.example.demo.Exceptions;

public class BalanceException extends RuntimeException{
    public BalanceException() {
    }

    public BalanceException(String message) {
        super(message);
    }

    public BalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
