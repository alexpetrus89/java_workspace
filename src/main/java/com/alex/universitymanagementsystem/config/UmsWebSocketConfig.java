package com.alex.universitymanagementsystem.config;

/**
 * Configure Spring for STOMP messaging
 *
 * Ora che i componenti essenziali del servizio sono stati creati,
 * è possibile configurare Spring per abilitare la messaggistica
 * WebSocket e STOMP.
 *
 * Creare una classe Java denominata WebSocketConfig.
 *
 * WebSocketConfig è annotato con @Configuration per indicare che si
 * tratta di una classe di configurazione Spring.
 *
 * È anche annotato con @EnableWebSocketMessageBroker.
 * Come suggerisce il nome, @EnableWebSocketMessageBroker abilita la
 * gestione dei messaggi WebSocket, supportata da un broker di messaggi.
 *
 * Il metodo configureMessageBroker() implementa il metodo predefinito in
 * WebSocketMessageBrokerConfigurer per configurare il broker di messaggi.
 * Inizia chiamando enableSimpleBroker() per abilitare un semplice broker
 * di messaggi basato sulla memoria a inoltrare i messaggi di saluto al
 * client su destinazioni con prefisso /topic.
 * Indica inoltre il prefisso /app per i messaggi associati a metodi annotati
 * con @MessageMapping.
 * Questo prefisso verrà utilizzato per definire tutti i mapping dei messaggi.
 * Ad esempio, /app/notify è l'endpoint che il metodo:
 *   ExaminationOutcomeController#notifyOutcome()
 * annotato con @MessageMapping("/notify") è mappato per gestire.
 *
 * Il metodo registerStompEndpoints() registra l'endpoint /ws per le
 * connessioni websocket.
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class UmsWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic/notify");
    }

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

}
