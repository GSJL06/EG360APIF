package com.educagestor.exception;

/**
 * Exception thrown when a request is malformed or contains invalid data
 * 
 * This exception is used for validation errors and business logic violations
 * that result from client-side errors.
 */
public class BadRequestException extends RuntimeException {

    /**
     * Constructor with message only
     * 
     * @param message the exception message
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * 
     * @param message the exception message
     * @param cause the underlying cause
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
