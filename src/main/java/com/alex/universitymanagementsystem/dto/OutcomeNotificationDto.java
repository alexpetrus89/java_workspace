package com.alex.universitymanagementsystem.dto;

import java.time.LocalDateTime;

import com.alex.universitymanagementsystem.domain.OutcomeNotification;

public record OutcomeNotificationDto(Long id, String message, LocalDateTime createdAt, LocalDateTime expiresAt) {
    public static OutcomeNotificationDto toDto(OutcomeNotification notification) {
        if (notification == null) return null;
        return new OutcomeNotificationDto(
                notification.getId(),
                notification.getMessage(),
                notification.getCreatedAt(),
                notification.getExpiresAt()
        );
    }
}

