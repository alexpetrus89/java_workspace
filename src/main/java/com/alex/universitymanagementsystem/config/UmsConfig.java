package com.alex.universitymanagementsystem.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;



@Configuration
public class UmsConfig {

    @Bean
	LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
	}


    @Bean
    String genericExceptionUri() {
        return "/exception/generic-exception";
    }


    // commons uri
    @Bean
    String dataAccessExceptionUri() {
        return "/exception/data/data-access-exception";
    }

    @Bean
    String jsonProcessingExceptionUri() {
        return "/exception/data/json-processing-exception";
    }

    @Bean
    String accessDeniedExceptionUri() {
        return "/exception/access_denied/access-denied-exception";
    }

    @Bean
    String illegalArgumentExceptionUri() {
        return "/exception/illegal";
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
    String duplicateUsernameUri() {
        return "/exception/illegal/invalid/duplicate-username";
    }

    @Bean
    String duplicateFiscalCodeUri() {
        return "/exception/illegal/invalid/duplicate-fiscal-code";
    }





}



