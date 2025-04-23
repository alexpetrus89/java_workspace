package com.alex.universitymanagementsystem.component;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

@Component
public class UmsWebSocketDecorator extends WebSocketHandlerDecorator {

    public UmsWebSocketDecorator(WebSocketHandler handler) {
        super(handler);
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        // Codice da eseguire dopo che la connessione WebSocket è stata stabilita
        System.out.println("Connessione WebSocket stabilita");
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session,@NonNull WebSocketMessage<?> message) throws Exception {
        // Codice da eseguire quando arriva un messaggio WebSocket
        System.out.println("Messaggio WebSocket ricevuto: " + message.getPayload());
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session,@NonNull Throwable exception) throws Exception {
        // Codice da eseguire quando si verifica un errore di trasporto WebSocket
        System.out.println("Errore di trasporto WebSocket: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session,@NonNull CloseStatus status) throws Exception {
        // Codice da eseguire dopo che la connessione WebSocket è stata chiusa
        System.out.println("Connessione WebSocket chiusa");
    }
}
