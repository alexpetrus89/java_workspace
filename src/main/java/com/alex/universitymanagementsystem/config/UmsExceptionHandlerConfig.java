package com.alex.universitymanagementsystem.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alex.universitymanagementsystem.exception.GlobalControllerExceptionHandler;

@Configuration
public class UmsExceptionHandlerConfig {

    @Bean
    GlobalControllerExceptionHandler globalControllerExceptionHandler() {
        return new GlobalControllerExceptionHandler();
    }

}
