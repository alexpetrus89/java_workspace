package com.alex.universitymanagementsystem.service;

import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.UsernameInvalidException;

public interface RegistrationService {

    /**
     * Check if the username is already in use by other users
     * @param username
     * @return true if the username is already in use, false otherwise
     * @throws UsernameInvalidException if the username is blank
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    boolean isUsernameAlreadyTaken(String username)
        throws UsernameInvalidException, DataAccessServiceException;

}
