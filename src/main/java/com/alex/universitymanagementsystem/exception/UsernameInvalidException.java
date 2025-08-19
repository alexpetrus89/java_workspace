package com.alex.universitymanagementsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UsernameInvalidException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UsernameInvalidException(String message) {
        super(message);
    }
}
