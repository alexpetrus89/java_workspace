package com.alex.universitymanagementsystem.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.entity.Notification;
import com.alex.universitymanagementsystem.entity.User;

import jakarta.persistence.PersistenceException;

@Repository
public interface NotificationRepository
    extends JpaRepository<Notification, Long>{

    /**
     * Retrieve all student's notification
     * @param user
     * @param now
     * @return Outcome notification
     * @throws PersistenceException persistence error
     */
    List<Notification> findByUserAndReadFalseAndExpiresAtAfter(User user, LocalDateTime now);


    /**
     * Delete all expired notifications
     * @param now
     * @throws PersistenceException persistence error
     */
    @Modifying
    void deleteByExpiresAtBefore(LocalDateTime now);

}
