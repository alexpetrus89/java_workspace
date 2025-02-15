package com.alex.studentmanagementsystem.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alex.studentmanagementsystem.exception.GlobalExceptionHandler;

@Configuration
public class ExceptionHandlerConfig {

    @Bean
    GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

}
