package com.alex.universitymanagementsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class UmsWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // MessageBrokerRegistry fornisce metodi per configurare il message broker.
    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {
        // Abilita il message broker per gestire le richieste di messaggistica per il topic /topic/notify.
        //Il topic è un canale di comunicazione che consente ai client di inviare e ricevere messaggi.
        config.enableSimpleBroker("/topic/notify");
        // Imposta il prefisso per le destinazioni dell'applicazione.
        config.setApplicationDestinationPrefixes("/ums");
    }

    // Il metodo registerStompEndpoints è un metodo di configurazione per i punti di
    // estremità STOMP (Simple Text-Oriented Messaging Protocol) in un'applicazione Spring
    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        registry.addEndpoint("/ums-ws");
    }

}
