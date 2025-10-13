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
        // Prefix for the broker (client -> server)
        config.setApplicationDestinationPrefixes("/ums");

        // Prefix for the internal topic/broker (server -> client)
        config.enableSimpleBroker("/topic");

        // Prefix for the public topic/broker (server -> client)
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
