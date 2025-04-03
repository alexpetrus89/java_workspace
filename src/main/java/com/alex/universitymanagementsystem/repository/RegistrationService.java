package com.alex.universitymanagementsystem.repository;

import org.springframework.lang.NonNull;

public interface RegistrationService {

    /**
     * Check if the username is already in use by other users
     * @param username
     * @return true if the username is already in use, false otherwise
     * @throws NullPointerException if the username is null
     * @throws IllegalArgumentException if the username is blank
     */
    boolean isUsernameAlreadyTaken(@NonNull String username);

}
