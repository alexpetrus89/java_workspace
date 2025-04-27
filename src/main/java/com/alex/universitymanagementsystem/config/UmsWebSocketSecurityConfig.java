package com.alex.universitymanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;


@Configuration
@EnableWebSocketSecurity
public class UmsWebSocketSecurityConfig {

    // constant
	private static final String ADMIN = "ADMIN";
	private static final String STUDENT = "STUDENT";
	private static final String PROFESSOR = "PROFESSOR";

    @Bean
    AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        messages
            .simpDestMatchers("/topic/**").hasAnyRole(PROFESSOR, ADMIN, STUDENT)
            .anyMessage().authenticated();

        return messages.build();
    }
}

