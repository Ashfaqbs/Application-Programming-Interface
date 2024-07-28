package com.ashfaq.dev.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleProductNotFoundException(ProductNotFoundException ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

	 @ExceptionHandler(InvalidProductIdException.class)
	    public ResponseEntity<CustomErrorResponse> handleInvalidProductIdException(InvalidProductIdException ex, WebRequest request) {
	        CustomErrorResponse errorResponse = new CustomErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
	        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	    }
	 

	@ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(LocalDateTime.now(), "An unexpected error occurred  : " + ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	
	
	
}
