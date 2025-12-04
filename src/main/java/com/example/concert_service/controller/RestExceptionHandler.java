package com.example.concert_service.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler for all REST controllers
 * Handles standard exceptions and provides standardized error responses
 */
@RestControllerAdvice
@Slf4j
@Hidden // Oculta este controlador de la documentación Swagger
public class RestExceptionHandler {

    /**
     * Handles EntityNotFoundException - Returns 404 Not Found
     * Se dispara cuando no se encuentra una entidad JPA en la base de datos
     * 
     * @param ex The EntityNotFoundException thrown
     * @return ResponseEntity with error details and NOT_FOUND status
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex) {
        log.error("Entity not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error("NOT_FOUND", ex.getMessage()));
    }

    /**
     * Handles NoSuchElementException - Returns 404 Not Found
     * Se dispara cuando un elemento no existe en una colección o Optional
     * 
     * @param ex The NoSuchElementException thrown
     * @return ResponseEntity with error details and NOT_FOUND status
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElement(NoSuchElementException ex) {
        log.error("Element not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error("NOT_FOUND", ex.getMessage()));
    }

    /**
     * Handles IllegalStateException - Returns 400 Bad Request
     * Se dispara cuando el estado de la aplicación no permite la operación
     * (ej: límites alcanzados, duplicados, etc.)
     * 
     * @param ex The IllegalStateException thrown
     * @return ResponseEntity with error details and BAD_REQUEST status
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalState(IllegalStateException ex) {
        log.error("Illegal state: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error("BAD_REQUEST", ex.getMessage()));
    }

    /**
     * Handles IllegalArgumentException - Returns 400 Bad Request
     * Se dispara cuando se proporciona un argumento inválido
     * 
     * @param ex The IllegalArgumentException thrown
     * @return ResponseEntity with error details and BAD_REQUEST status
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        log.error("Invalid argument: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error("INVALID_ARGUMENT", ex.getMessage()));
    }

    /**
     * Handles DataIntegrityViolationException - Returns 409 Conflict
     * Se dispara cuando hay una violación de integridad de datos (ej: duplicados,
     * FK)
     * 
     * @param ex The DataIntegrityViolationException thrown
     * @return ResponseEntity with error details and CONFLICT status
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleConflict(DataIntegrityViolationException ex) {
        log.error("Data integrity violation: {}", ex.getMessage());

        // Extraer el mensaje más legible de la excepción
        String message = ex.getMostSpecificCause().getMessage();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error("CONFLICT", message));
    }

    /**
     * Handles MethodArgumentNotValidException - Returns 400 Bad Request
     * Se dispara cuando la validación de @Valid/@Validated falla en los DTOs
     * 
     * @param ex The MethodArgumentNotValidException thrown
     * @return ResponseEntity with error details including field-specific validation
     *         errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());

        // Crear el mapa de errores base
        Map<String, Object> body = error("VALIDATION_ERROR", "Validation failed");

        // Extraer los errores de validación por campo
        Map<String, String> fields = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fields.put(fe.getField(), fe.getDefaultMessage());
        }

        // Agregar los errores de campo al cuerpo de la respuesta
        body.put("fields", fields);

        return ResponseEntity
                .badRequest()
                .body(body);
    }

    // ==================== EXCEPCIONES DE SEGURIDAD ====================

    /**
     * Handles AuthenticationException - Returns 401 Unauthorized
     * Se dispara cuando la autenticación falla (credenciales incorrectas)
     * 
     * @param ex The AuthenticationException thrown
     * @return ResponseEntity with error details and UNAUTHORIZED status
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthentication(AuthenticationException ex) {
        log.error("Authentication failed: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error("UNAUTHORIZED", "Invalid credentials"));
    }

    /**
     * Handles BadCredentialsException - Returns 401 Unauthorized
     * Se dispara específicamente cuando las credenciales son inválidas
     * 
     * @param ex The BadCredentialsException thrown
     * @return ResponseEntity with error details and UNAUTHORIZED status
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex) {
        log.error("Bad credentials: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error("UNAUTHORIZED", "Invalid email or password"));
    }

    /**
     * Handles AccessDeniedException - Returns 403 Forbidden
     * Se dispara cuando el usuario autenticado no tiene permisos suficientes
     * 
     * @param ex The AccessDeniedException thrown
     * @return ResponseEntity with error details and FORBIDDEN status
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
        log.error("Access denied: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error("FORBIDDEN", "You don't have permission to access this resource"));
    }

    /**
     * Handles generic exceptions - Returns 500 Internal Server Error
     * Maneja cualquier excepción no capturada específicamente
     * 
     * @param ex The Exception thrown
     * @return ResponseEntity with generic error message and INTERNAL_SERVER_ERROR
     *         status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred: ", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error("INTERNAL_SERVER_ERROR", "An unexpected error occurred. Please try again later."));
    }

    /**
     * Creates a standardized error response map
     * Crea un mapa estándar para las respuestas de error
     * 
     * @param code    The error code identifier
     * @param message The error message description
     * @return Map containing timestamp, code, and message
     */
    private Map<String, Object> error(String code, String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", Instant.now().toString());
        map.put("code", code);
        map.put("message", message);
        return map;
    }
}
