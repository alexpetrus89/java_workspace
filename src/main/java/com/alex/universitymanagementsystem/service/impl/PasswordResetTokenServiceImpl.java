package com.alex.universitymanagementsystem.service.impl;

import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.PasswordResetToken;
import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.repository.PasswordResetTokenRepository;
import com.alex.universitymanagementsystem.repository.UserRepository;
import com.alex.universitymanagementsystem.service.PasswordResetTokenService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;


@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {



    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetTokenServiceImpl(
        UserRepository userRepository,
        PasswordResetTokenRepository tokenRepository,
        JavaMailSender mailSender,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * Sends a password reset link to the user's email.
     * @param email the email address of the user
     * @throws IllegalArgumentException if no user is found with the given email
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Override
    public void sendPasswordResetLink(String email) {

        try {
            User user = userRepository
                .findByUsername(email)
                .orElseThrow(() -> new IllegalArgumentException("No user with email " + email));

            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken(token, user, 30);
            tokenRepository.save(resetToken);

            String resetLink = "http://localhost:8081/reset-password?token=" + token;
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getUsername());
            message.setSubject("Password Reset");
            message.setText("Click here to reset your password: " + resetLink);
            mailSender.send(message);
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

