package com.alex.universitymanagementsystem.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.universitymanagementsystem.dto.NotificationDto;
import com.alex.universitymanagementsystem.entity.User;
import com.alex.universitymanagementsystem.service.NotificationService;

@RestController
@RequestMapping(path = "api/v1/notification")
public class NotificationController {

    // instance variable
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    /**
     * This method is used to get the notifications of a student
     * @param student
     * @return a list of OutcomeNotificationDto
     */
    @GetMapping("/read/all")
    public List<NotificationDto> getAllUserNotifications(@AuthenticationPrincipal User user) {
        return notificationService
            .getActiveNotifications(user)
            .stream()
            .map(NotificationDto::toDto)
            .toList();
    }



    /**
     * This method is used to mark a notification as read
     * @param id
     * @return a ResponseEntity
     */
    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

}
