package com.alex.universitymanagementsystem.service;

import org.springframework.messaging.MessagingException;

public interface WebSocketService {

    /**
     * Sends a WebSocket message to a specific user.
     * @param username
     * @param destination
     * @param message
     * @throws MessagingException if an error occurs while sending the message
     */
    public void sendWebSocketMessage(String username, String destination, String message)
        throws MessagingException;

}
