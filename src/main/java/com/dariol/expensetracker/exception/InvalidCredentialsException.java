package com.dariol.expensetracker.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Incorrect email or password.");
    }
}
