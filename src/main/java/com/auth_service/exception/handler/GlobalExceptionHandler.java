package com.auth_service.exception.handler;

import com.auth_service.exception.ExternalAuthServiceException;
import com.auth_service.exception.InvalidRefreshTokenException;
import com.auth_service.exception.RegistrationDetailsConflictException;
import com.auth_service.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExternalAuthServiceException.class)
    public ResponseEntity<Map<String, String>> handleExternalAuthServiceException(ExternalAuthServiceException exception) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<Map<String, String>> handleInvalidRefreshTokenException(InvalidRefreshTokenException exception) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(RegistrationDetailsConflictException.class)
    public ResponseEntity<Map<String, String>> handleRegistrationDetailsConflictException(RegistrationDetailsConflictException exception) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException exception) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", exception.getMessage()));
    }
}
