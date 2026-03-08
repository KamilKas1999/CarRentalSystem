package com.kasprzak.kamil.CarRentalSystem.exceptions;

public class CarNotAvailableException extends RuntimeException {
    public CarNotAvailableException(String message) {
        super(message);
    }
}
