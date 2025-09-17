package com.alex.universitymanagementsystem.exception;


import java.io.IOException;



public class JsonProcessingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // instance variables
    private final String message;

    // Constructors
    public JsonProcessingException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    // Constructors
    public JsonProcessingException(String message, IOException e) {
        super(message, e);
        this.message = message;
    }

    // getter
    public String getBaseMessage() {
        return message;
    }

}
