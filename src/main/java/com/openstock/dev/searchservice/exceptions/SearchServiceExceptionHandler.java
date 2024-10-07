package com.openstock.dev.searchservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.openstock.dev.searchservice.exceptions.SearchServiceExceptions.*;

@RestControllerAdvice
public class SearchServiceExceptionHandler {

    // Handle ProductNotFoundException
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Handle ProductAlreadyBoostedException
    @ExceptionHandler(ProductAlreadyBoostedException.class)
    public ResponseEntity<String> handleProductAlreadyBoostedException(ProductAlreadyBoostedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // Handle ProductNotBoostedException
    @ExceptionHandler(ProductNotBoostedException.class)
    public ResponseEntity<String> handleProductNotBoostedException(ProductNotBoostedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Generic handler for any other unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
