package com.alex.universitymanagementsystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.messaging.MessagingException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.entity.Notification;
import com.alex.universitymanagementsystem.entity.Student;
import com.alex.universitymanagementsystem.entity.User;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.repository.NotificationRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.service.EmailService;
import com.alex.universitymanagementsystem.service.NotificationService;
import com.alex.universitymanagementsystem.service.WebSocketService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class NotificationServiceImpl implements NotificationService {
    // instance variables
    private final EmailService emailService;
    private final WebSocketService webSocketService;
    private final NotificationRepository notificationRepository;
    private final StudentRepository studentRepository;

    public NotificationServiceImpl(
        WebSocketService webSocketService,
        EmailService emailService,
        NotificationRepository notificationRepository,
        StudentRepository studentRepository
    ) {
        this.webSocketService = webSocketService;
        this.emailService = emailService;
        this.notificationRepository = notificationRepository;
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

            // persistence
            Student student = studentRepository
                .findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.STUDENT));
            Notification notification = new Notification();
            notification.setUser(student);
            notification.setMessage(message);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setExpiresAt(LocalDateTime.now().plusDays(3));
            notification.setRead(false);
            notificationRepository.save(notification);

            // send WebSocket notification
            emailService.sendEmail(username, "Exam outcome notification", message);
            webSocketService.sendWebSocketMessage(username, "/topic/exam-outcome", message);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching notifications: ", e);
        }
    }


    /**
     * Retrieves the active notifications for a specific student.
     * @param user the user for whom to retrieve notifications
     * @return a list of active notifications for the student
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Override
    public List<Notification> getActiveNotifications(User user)
        throws DataAccessServiceException {

        try {
            return notificationRepository
                .findByUserAndReadFalseAndExpiresAtAfter(user, LocalDateTime.now());
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
            notificationRepository
                .findById(notificationId)
                .ifPresent(n -> {
                    n.setRead(true);
                    notificationRepository.save(n);
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
            notificationRepository.deleteByExpiresAtBefore(LocalDateTime.now());
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for cleaning expired notifications: ", e);
        }
    }


}

