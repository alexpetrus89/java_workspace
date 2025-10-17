package com.alex.universitymanagementsystem.dto;

import java.time.LocalDateTime;

import com.alex.universitymanagementsystem.entity.Notification;

public record NotificationDto(Long id, String message, LocalDateTime createdAt, LocalDateTime expiresAt) {
    public static NotificationDto toDto(Notification notification) {
        if (notification == null) return null;
        return new NotificationDto(
                notification.getId(),
                notification.getMessage(),
                notification.getCreatedAt(),
                notification.getExpiresAt()
        );
    }
}

