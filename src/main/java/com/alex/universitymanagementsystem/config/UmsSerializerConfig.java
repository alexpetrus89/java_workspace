package com.alex.universitymanagementsystem.config;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.StreamWriteConstraints;

@Configuration
@EnableWebMvc
@JsonComponent
public class UmsSerializerConfig {

    // Aumenta la profondità di nidificazione massima a 2000
    @Bean
    StreamWriteConstraints streamWriteConstraints() {
        return StreamWriteConstraints
            .builder()
            .maxNestingDepth(2000)
            .build();
    }

}
