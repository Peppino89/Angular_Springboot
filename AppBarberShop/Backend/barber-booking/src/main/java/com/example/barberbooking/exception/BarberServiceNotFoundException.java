package com.example.barberbooking.exception;

public class BarberServiceNotFoundException extends RuntimeException {

    public BarberServiceNotFoundException(String message) {
        super(message);
    }
}
