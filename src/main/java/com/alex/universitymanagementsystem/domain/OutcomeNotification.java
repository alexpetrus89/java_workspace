package com.alex.universitymanagementsystem.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "OUTCOME_NOTIFICATIONS")
@Access(AccessType.PROPERTY)
public class OutcomeNotification implements Serializable {

    // instance variables
    private Long id;
    private Student student;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean read; // true se lo studente ha già accettato l’esito

    // constructors
    public OutcomeNotification() {}

    // getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    @ManyToOne
    public Student getStudent() {
        return student;
    }

    @Column(nullable = false)
    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public boolean isRead() {
        return read;
    }

    // setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
