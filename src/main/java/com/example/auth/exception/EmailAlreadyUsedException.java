package com.example.auth.exception;

public class EmailAlreadyUsedException extends RuntimeException{

    private static final String MESSAGE = "Email is already used: %s";

    public EmailAlreadyUsedException(String email) {
        super(String.format(MESSAGE, email));
    }

}
