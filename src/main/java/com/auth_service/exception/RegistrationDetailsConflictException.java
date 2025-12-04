package com.auth_service.exception;

public class RegistrationDetailsConflictException extends RuntimeException{
    public RegistrationDetailsConflictException(String message) {
        super(message);
    }
}
