package com.alex.universitymanagementsystem.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.entity.OutcomeNotification;
import com.alex.universitymanagementsystem.entity.Student;

import jakarta.persistence.PersistenceException;

@Repository
public interface OutcomeNotificationRepository
    extends JpaRepository<OutcomeNotification, Long>{

    /**
     * Retrieve all student's notification
     * @param student
     * @param now
     * @return Outcome notification
     * @throws PersistenceException persistence error
     */
    List<OutcomeNotification> findByStudentAndReadFalseAndExpiresAtAfter(Student student, LocalDateTime now);


    /**
     * Delete all expired notifications
     * @param now
     * @throws PersistenceException persistence error
     */
    @Modifying
    void deleteByExpiresAtBefore(LocalDateTime now);

}
