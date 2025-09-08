package com.alex.universitymanagementsystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "PASSWORD_RESET_TOKENS")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;

    @Column(name = "expiry_date", nullable = false, unique = true)
    private LocalDateTime expiryDate;

    @Column(name = "used", nullable = false, unique = true)
    private boolean used = false;

    // constructors
    public PasswordResetToken() {}

    public PasswordResetToken(String token, User user, int minutes) {
        this.token = token;
        this.user = user;
        this.expiryDate = LocalDateTime.now().plusMinutes(minutes);
    }

    // getters
    public Long getId() { return id; }
    public String getToken() { return token; }
    public User getUser() { return user; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public boolean isUsed() { return used; }
    public boolean isExpired() { return LocalDateTime.now().isAfter(expiryDate) || used; }

    // setters
    public void setId(Long id) { this.id = id; }
    public void setToken(String token) { this.token = token; }
    public void setUser(User user) { this.user = user; }
    public void setExpiryDate(LocalDateTime date) { this.expiryDate = date; }
    public void setUsed(boolean used) { this.used = used; }

}
