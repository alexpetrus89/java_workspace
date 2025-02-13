package com.alex.studentmanagementsystem.config;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentDatabaseConfig implements Serializable {

    @Value("${spring.datasource.password}")
    private final String password;

    public StudentDatabaseConfig() {
        password = System.getenv("DB_PASSWORD");
        // Use the password value in your code...
    }
}
