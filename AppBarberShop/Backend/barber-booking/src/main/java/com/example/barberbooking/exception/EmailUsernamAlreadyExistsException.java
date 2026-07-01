package com.example.barberbooking.exception;

public class EmailUsernamAlreadyExistsException extends RuntimeException {
    public EmailUsernamAlreadyExistsException(String message) {
        super(message);
    }
}
