package com.alex.universitymanagementsystem.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.dto.OutcomeNotificationDto;
import com.alex.universitymanagementsystem.service.OutcomeNotificationService;

@RestController
@RequestMapping(path = "api/v1/outcome-notifications")
public class OutcomeNotificationController {

    // instance variable
    private final OutcomeNotificationService outcomeNotificationService;

    public OutcomeNotificationController(OutcomeNotificationService outcomeNotificationService) {
        this.outcomeNotificationService = outcomeNotificationService;
    }


    /**
     * This method is used to get the notifications of a student
     * @param student
     * @return a list of OutcomeNotificationDto
     */
    @GetMapping
    public List<OutcomeNotificationDto> getStudentNotifications(@AuthenticationPrincipal Student student) {
        return outcomeNotificationService.getActiveNotifications(student)
            .stream()
            .map(OutcomeNotificationDto::toDto)
            .toList();
    }



    /**
     * This method is used to mark a notification as read
     * @param id
     */
    @PostMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        outcomeNotificationService.markAsRead(id);
    }

}
