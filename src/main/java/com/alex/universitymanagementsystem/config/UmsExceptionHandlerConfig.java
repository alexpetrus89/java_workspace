package com.alex.universitymanagementsystem.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alex.universitymanagementsystem.exception.GlobalExceptionHandler;

@Configuration
public class UmsExceptionHandlerConfig {

    @Bean
    GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

}
