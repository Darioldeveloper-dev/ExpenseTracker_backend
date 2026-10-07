package com.dariol.expensetracker.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("An account with the email '" + email + "' already exists.");
    }
}
