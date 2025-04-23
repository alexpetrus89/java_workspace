package com.alex.universitymanagementsystem.config;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UmsDBConfig implements Serializable {

    @Value("${spring.datasource.password}")
    private final String password;

    public UmsDBConfig() {
        // get password from environment variable
        // This is a placeholder. In a real application, you would use a secure method to retrieve the password.
        password = System.getenv("DB_PASSWORD");
    }

    public String getPassword() {
        return password;
    }
}
