package com.alex.universitymanagementsystem.component;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UmsJsonConfigLoader {

    private final ObjectMapper objectMapper;

    public UmsJsonConfigLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T loadJson(String resourcePath, Class<T> clazz) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null)
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            return objectMapper.readValue(inputStream, clazz);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read JSON resource: " + resourcePath, e);
        }
    }

}

