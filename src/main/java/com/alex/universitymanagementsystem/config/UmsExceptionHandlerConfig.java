package com.alex.universitymanagementsystem.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alex.universitymanagementsystem.exception.GlobalControllerExceptionHandler;

@Configuration
public class UmsExceptionHandlerConfig {

    // instance variables
    private final UmsConfig umsConfig;

    public UmsExceptionHandlerConfig(UmsConfig umsConfig) {
        this.umsConfig = umsConfig;
    }

     @Bean
    GlobalControllerExceptionHandler globalControllerExceptionHandler() {
        return new GlobalControllerExceptionHandler(umsConfig);
    }

}
