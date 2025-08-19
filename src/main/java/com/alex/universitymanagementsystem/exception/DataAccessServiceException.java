package com.alex.universitymanagementsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class DataAccessServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    public DataAccessServiceException(String message) {
        super(message);
    }

    public DataAccessServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessServiceException(Throwable cause) {
        super(cause);
    }
}
