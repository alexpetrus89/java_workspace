package com.alex.universitymanagementsystem.config;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alex.universitymanagementsystem.component.UmsExitCodeGenerator;


@Configuration
public class UmsConfig {

    // Define and inject applicationContext
    private final ApplicationContext applicationContext;

    public UmsConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    ExitCodeGenerator exitCodeGenerator() {
        return new UmsExitCodeGenerator();
    }

    public void shutDown(ExitCodeGenerator exitCodeGenerator) {
        SpringApplication.exit(applicationContext, exitCodeGenerator);
    }

    @Bean
    String genericExceptionUri() {
        return "/exception/error";
    }

    @Bean
    String dataAccessExceptionUri() {
        return "/exception/data/data-access-error";
    }

    @Bean
    String accessDeniedExceptionUri() {
        return "/exception/access_denied";
    }

    @Bean
    String notFoundExceptionUri() {
        return "/exception/not_found";
    }

    @Bean
    String alreadyExistsExceptionUri() {
        return "/exception/already_exists";
    }

    @Bean
    String illegalArgumentExceptionUri() {
        return "/exception/illegal";
    }



}
