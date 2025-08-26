package com.alex.universitymanagementsystem.service;

import java.util.List;

import org.springframework.messaging.MessagingException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;

import com.alex.universitymanagementsystem.domain.OutcomeNotification;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;


public interface OutcomeNotificationService {


    /**
     * Notifies the specified user of the result of a test.
     * @param username the student's username
     * @param message the notification message
     * @throws MessagingException if an error occurs while sending the message
     * @throws ObjectNotFoundException if the student is not found
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Transactional(rollbackOn = {MessagingException.class, ObjectNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    void notifyExamOutcome(String username, String message)
        throws MessagingException, ObjectNotFoundException, DataAccessServiceException;


    /**
     * Retrieves the active notifications for a specific student.
     * @param student the student for whom to retrieve notifications
     * @return a list of active notifications for the student
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    List<OutcomeNotification> getActiveNotifications(Student student) throws DataAccessServiceException;


    /**
     * Marks a notification as read.
     * @param notificationId the ID of the notification to mark as read
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Transactional
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    void markAsRead(Long notificationId) throws DataAccessServiceException;


    /**
     * Pulizia notifiche scadute: viene eseguita ogni notte alle 2:00
     * @throws DataAccessServiceException
     */
    @Transactional
    @Scheduled(cron = "0 0 2 * * ?")
    void cleanExpiredNotifications() throws DataAccessServiceException;

}
