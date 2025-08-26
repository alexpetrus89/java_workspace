package com.alex.universitymanagementsystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.OutcomeNotification;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.repository.OutcomeNotificationRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.service.OutcomeNotificationService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class OutcomeNotificationServiceImpl implements OutcomeNotificationService {
    // instance variables
    private final SimpMessagingTemplate messagingTemplate;
    private final OutcomeNotificationRepository outcomeNotificationRepository;
    private final StudentRepository studentRepository;

    public OutcomeNotificationServiceImpl(
        SimpMessagingTemplate messagingTemplate,
        OutcomeNotificationRepository outcomeNotificationRepository,
        StudentRepository studentRepository
    ) {
        this.messagingTemplate = messagingTemplate;
        this.outcomeNotificationRepository = outcomeNotificationRepository;
        this.studentRepository = studentRepository;
    }


    /**
     * Notifies the specified user of the result of a test.
     * @param username the student's username
     * @param message the notification message
     * @throws MessagingException if an error occurs while sending the message
     * @throws ObjectNotFoundException if the student is not found
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Override
    @Transactional(rollbackOn = {MessagingException.class, ObjectNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void notifyExamOutcome(String username, String message)
        throws MessagingException, ObjectNotFoundException, DataAccessServiceException {

        try {
            // send WebSocket notification
            messagingTemplate.convertAndSendToUser(username, "/topic/exam-outcome", message);

            // persistenza su DB
            Student student = studentRepository
                .findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.STUDENT));
            OutcomeNotification notification = new OutcomeNotification();
            notification.setStudent(student);
            notification.setMessage(message);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setExpiresAt(LocalDateTime.now().plusDays(3));
            notification.setRead(false);

            outcomeNotificationRepository.save(notification);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching notifications: ", e);
        }
    }


    /**
     * Retrieves the active notifications for a specific student.
     * @param student the student for whom to retrieve notifications
     * @return a list of active notifications for the student
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Override
    public List<OutcomeNotification> getActiveNotifications(Student student)
        throws DataAccessServiceException {

        try {
            return outcomeNotificationRepository
                .findByStudentAndReadFalseAndExpiresAtAfter(student, LocalDateTime.now());
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching notifications: ", e);
        }
    }


    /**
     * Marks a notification as read.
     * @param notificationId the ID of the notification to mark as read
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Override
    @Transactional
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void markAsRead(Long notificationId) throws DataAccessServiceException {
        try {
            outcomeNotificationRepository
                .findById(notificationId)
                .ifPresent(n -> {
                    n.setRead(true);
                    outcomeNotificationRepository.save(n);
                });
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for marking notification as read: ", e);
        }
    }


    /**
     * Pulizia notifiche scadute: viene eseguita ogni notte alle 2:00
     * @throws DataAccessServiceException
     */
    @Override
    @Transactional
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanExpiredNotifications() throws DataAccessServiceException {
        try {
            outcomeNotificationRepository.deleteByExpiresAtBefore(LocalDateTime.now());
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for cleaning expired notifications: ", e);
        }
    }
}

