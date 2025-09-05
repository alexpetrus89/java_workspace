package com.alex.universitymanagementsystem.exception;


public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException(String username) {
        super("Username '" + username + "' is already taken by another user");
    }

}

