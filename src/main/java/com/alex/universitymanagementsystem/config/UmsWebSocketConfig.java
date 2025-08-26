package com.alex.universitymanagementsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class UmsWebSocketConfig implements WebSocketMessageBrokerConfigurer{

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry config){
        // Prefisso per il broker (client -> server)
        config.setApplicationDestinationPrefixes("/ums");

        // Prefisso per il topic/broker interno (server -> client)
        config.enableSimpleBroker("/topic");

        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        registry
            .addEndpoint("/ws")
            .setAllowedOriginPatterns("http://localhost:8081")
            .withSockJS();
    }

}
