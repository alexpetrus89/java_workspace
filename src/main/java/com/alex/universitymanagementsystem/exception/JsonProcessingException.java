package com.alex.universitymanagementsystem.exception;


import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class JsonProcessingException extends RuntimeException {

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
