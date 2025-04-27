package com.alex.universitymanagementsystem.service;

import org.springframework.lang.NonNull;

import com.alex.universitymanagementsystem.exception.UsernameInvalidException;

public interface RegistrationService {

    /**
     * Check if the username is already in use by other users
     * @param username
     * @return true if the username is already in use, false otherwise
     * @throws NullPointerException if the username is null
     * @throws UsernameInvalidException if the username is blank
     */
    boolean isUsernameAlreadyTaken(@NonNull String username);

}
