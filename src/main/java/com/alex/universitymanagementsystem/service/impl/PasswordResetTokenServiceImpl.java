package com.alex.universitymanagementsystem.service.impl;

import java.util.UUID;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.entity.PasswordResetToken;
import com.alex.universitymanagementsystem.entity.User;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.repository.PasswordResetTokenRepository;
import com.alex.universitymanagementsystem.repository.UserRepository;
import com.alex.universitymanagementsystem.service.EmailService;
import com.alex.universitymanagementsystem.service.PasswordResetTokenService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;


@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetTokenServiceImpl(
        UserRepository userRepository,
        PasswordResetTokenRepository tokenRepository,
        EmailService emailService,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * Sends a password reset link to the user's email.
     * @param email the email address of the user
     * @throws UsernameNotFoundException if no user is found with the given email
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Override
    public void sendPasswordResetLink(String email) {

        try {
            User user = userRepository
                .findByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user with email " + email));

            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken(token, user, 30);
            String resetLink = "http://localhost:8081/reset-password?token=" + token;
            tokenRepository.save(resetToken);
            emailService.sendEmail(user.getUsername(), "Password reset", "Click here to reset your password: " + resetLink);
        } catch (PersistenceException _) {
            throw new DataAccessServiceException("Error accessing database for fetching user");
        }
    }


    /**
     * Resets the user's password given the token and new password.
     * @param token the token provided by the user in the password reset link
     * @param newPassword the new password to set for the user
     * @throws IllegalArgumentException if the token is invalid, expired, or already used
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Override
    @Transactional(rollbackOn = IllegalArgumentException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void resetPassword(String token, String newPassword) {
        try {
            PasswordResetToken resetToken = tokenRepository
                .findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

            if (resetToken.isExpired())
                throw new IllegalArgumentException("Token expired or already used");

            User user = resetToken.getUser();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            resetToken.setUsed(true);
            tokenRepository.save(resetToken);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching user", e);
        }
    }


}

