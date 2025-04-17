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


}
