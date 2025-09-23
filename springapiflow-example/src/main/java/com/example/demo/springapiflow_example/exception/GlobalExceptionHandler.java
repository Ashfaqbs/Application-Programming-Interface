package com.example.demo.springapiflow_example.exception;

import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // unified error payload
    public record ErrorBody(String code, String message, Map<String, String> details, String timestamp) {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorBody> onValidation(MethodArgumentNotValidException ex) {
        Map<String, String> details = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            details.put(fe.getField(), fe.getDefaultMessage());
        }
        var body = new ErrorBody("VALIDATION_ERROR", "Invalid request body", details, OffsetDateTime.now().toString());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorBody> onNotFound(NotFoundException ex) {
        var body = new ErrorBody("NOT_FOUND", ex.getMessage(), Map.of(), OffsetDateTime.now().toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorBody> onGeneric(Exception ex) {
        var body = new ErrorBody("INTERNAL_ERROR", ex.getClass().getSimpleName(), Map.of(), OffsetDateTime.now().toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
