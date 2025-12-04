package com.example.concert_service.exception;

/**
 * Exception thrown when a requested resource is not found
 * Returns HTTP 404 - Not Found
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor with resource details
     * 
     * @param resource The resource type (e.g., "Profile", "Artist")
     * @param field    The field used for search (e.g., "id", "userId")
     * @param value    The value that was not found
     */
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s not found with %s: '%s'", resource, field, value));
    }

    /**
     * Constructor with custom message
     * 
     * @param message The error message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
