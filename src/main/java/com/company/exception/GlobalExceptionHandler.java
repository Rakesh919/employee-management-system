package com.company.exception;

import com.company.dto.response.ErrorResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle custom UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        logger.info("UserNotFoundException: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(ex.getMessage(), "ERR-404");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handle custom AuthenticationException
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        logger.warn("AuthenticationException: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(ex.getMessage(), "ERR-401");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Handle bad request / invalid arguments
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.warn("IllegalArgumentException: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ex.getMessage(), "ERR-400");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        logger.warn("Validation failed: {}", ex.getMessage());

        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = (fieldError != null) ? fieldError.getDefaultMessage() : "Validation failed";

        ErrorResponse response = new ErrorResponse(message, "VALIDATION_ERR");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle database/repository exceptions
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex, WebRequest request) {
        logger.error("Database error occurred", ex);
        ErrorResponse response = new ErrorResponse("Database operation failed", "ERR-500");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle all other uncaught exceptions - KEEP THIS AS FALLBACK
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("Unhandled exception occurred", ex);
        ErrorResponse response = new ErrorResponse("An unexpected error occurred", "INT-500");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // Add more handlers if needed...
}

