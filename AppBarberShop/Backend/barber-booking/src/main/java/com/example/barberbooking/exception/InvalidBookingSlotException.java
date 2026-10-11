package com.example.barberbooking.exception;

public class InvalidBookingSlotException extends RuntimeException {
    public InvalidBookingSlotException(String message) {
        super(message);
    }
}
