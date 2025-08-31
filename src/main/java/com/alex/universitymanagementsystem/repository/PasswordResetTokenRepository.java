package com.alex.universitymanagementsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.domain.PasswordResetToken;

import jakarta.persistence.PersistenceException;


@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    /**
     * Find a password reset token by its token string.
     * @param token the token string
     * @return Optional<PasswordResetToken>
     * @throws PersistenceException persistence error
     */
    Optional<PasswordResetToken> findByToken(String token);

}
