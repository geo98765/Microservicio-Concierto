package com.example.concert_service.exception;

/**
 * Exception thrown when a business rule is violated
 * Returns HTTP 400 - Bad Request
 */
public class BusinessRuleException extends RuntimeException {

    /**
     * Constructor with message
     * 
     * @param message The error message describing the business rule violation
     */
    public BusinessRuleException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * 
     * @param message The error message
     * @param cause   The underlying cause
     */
    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
