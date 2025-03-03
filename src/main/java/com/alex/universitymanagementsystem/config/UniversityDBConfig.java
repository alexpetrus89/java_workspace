package com.alex.universitymanagementsystem.config;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UniversityDBConfig implements Serializable {

    @Value("${spring.datasource.password}")
    private final String password;

    public UniversityDBConfig() {
        password = System.getenv("DB_PASSWORD");
        // Use the password value in your code...
    }
}
