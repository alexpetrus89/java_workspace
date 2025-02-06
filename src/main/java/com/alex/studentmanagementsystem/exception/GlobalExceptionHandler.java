package com.alex.studentmanagementsystem.exception;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.xml.sax.SAXException;


import java.io.IOException;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
        LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // Handle the exception and generate a custom error response
        logger.error("An error occurred", e);
        String errorMessage = "An error occurred: " + e.getMessage();

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorMessage);
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<String> handleInternalServerError(InternalServerError e) {
        // Handle the specific exception and generate a custom error response
        logger.error("Internal server error", e);
        String errorMessage = "Internal server error: " + e.getMessage();

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorMessage);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
        // Handle the specific exception and generate a custom error response
        logger.error("Resource not found", e);
        String errorMessage = "Resource not found: " + e.getMessage();

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorMessage);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNoResourceFoundException(NoResourceFoundException e) {
        // Handle the specific exception and generate a custom error response
        logger.error("Resource not found", e);
        String errorMessage = "Resource not found, sei uno stronzo: " + e.getMessage();

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorMessage);
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoStaticResourceException(NoHandlerFoundException e) {
        // Handle the specific exception and generate a custom error response
        logger.error("No handler found for request", e);
        String errorMessage = "No handler found for request: " + e.getMessage();

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorMessage);
    }


    @ExceptionHandler({ParseException.class, SAXException.class, IOException.class})
    public ResponseEntity<String> handleHtmlParseException(Exception e) {
        // Handle the specific exception and generate a custom error response
        logger.error("Error during HTML parsing", e);
        String errorMessage = "Error during HTML parsing, hai fatto qualche cazzata nel file .html: " + e.getMessage();

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorMessage);
    }


    // Add more @ExceptionHandler methods for other specific exceptions

}
