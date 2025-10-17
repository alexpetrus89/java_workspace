package com.alex.universitymanagementsystem.service.impl;

import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.service.WebSocketService;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    /**
     * Sends a WebSocket message to a specific user.
     * @param username
     * @param destination
     * @param message
     * @throws MessagingException if an error occurs while sending the message
     */
    @Override
    public void sendWebSocketMessage(String username, String destination, String message)
        throws MessagingException
    {
        messagingTemplate.convertAndSendToUser(username, destination, message);
    }

}
