package com.alex.universitymanagementsystem.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.alex.universitymanagementsystem.exception.DataAccessServiceException;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

public interface PasswordResetTokenService {

    /**
     * Sends a password reset link to the user's email.
     * @param email the email address of the user
     * @throws IllegalArgumentException if no user is found with the given email
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    void sendPasswordResetLink(String email);


    /**
     * Resets the user's password given the token and new password.
     * @param token the token provided by the user in the password reset link
     * @param newPassword the new password to set for the user
     * @throws IllegalArgumentException if the token is invalid, expired, or already used
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Transactional(rollbackOn = IllegalArgumentException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    void resetPassword(String token, String newPassword);

}
