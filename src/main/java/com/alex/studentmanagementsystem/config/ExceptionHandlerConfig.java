package com.alex.studentmanagementsystem.config;

import java.io.Serializable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alex.studentmanagementsystem.exception.GlobalExceptionHandler;

@Configuration
public class ExceptionHandlerConfig implements Serializable {

    @Bean
    GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

}
