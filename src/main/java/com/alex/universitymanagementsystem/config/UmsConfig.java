package com.alex.universitymanagementsystem.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.alex.universitymanagementsystem.component.UmsExitCodeGenerator;


@Configuration
public class UmsConfig {

    // instance variables
    private final ApplicationContext applicationContext;
    private final Map<String, String> fieldToView = new HashMap<>();

    public UmsConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;

        // campi principali del form di registrazione
        fieldToView.put("username", "exception/illegal/invalid/invalid-username");
        fieldToView.put("usernameAlreadyTaken", "exception/illegal/invalid/username-already-taken");

        // password + vincolo di classe @PasswordMatches
        fieldToView.put("password", "exception/illegal/invalid/invalid-password");
        fieldToView.put("passwordsDoNotMatch", "exception/illegal/invalid/password-not-match");

        fieldToView.put("firstName", "exception/illegal/invalid/invalid-first-name");
        fieldToView.put("lastName", "exception/illegal/invalid/invalid-last-name");
        fieldToView.put("dob", "exception/illegal/invalid/invalid-dob");
        fieldToView.put("fiscalCode", "exception/illegal/invalid/invalid-fiscal-code");

        // indirizzo
        final String invalidAddressView = "exception/illegal/invalid/invalid-address";
        fieldToView.put("street", invalidAddressView);
        fieldToView.put("city", invalidAddressView);
        fieldToView.put("state", invalidAddressView);
        fieldToView.put("zip", invalidAddressView);

        fieldToView.put("phone", "exception/illegal/invalid/invalid-phone");
        fieldToView.put("role", "exception/illegal/invalid/invalid-role");

        // fallback generico
        fieldToView.put("register", "exception/illegal/invalid-register");
    }

    /**
     * Restituisce la view corrispondente al campo.
     * Se non esiste mapping, restituisce la view di default.
     */
    public String resolveView(String fieldName) {
        return fieldToView.getOrDefault(fieldName, "exception/illegal/illegal-parameter");
    }

    @Bean
	LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
	}

    @Bean
    ExitCodeGenerator exitCodeGenerator() {
        return new UmsExitCodeGenerator();
    }


    /**
     * Shut down the application.
     */
    public void shutDown(ExitCodeGenerator exitCodeGenerator) {
        SpringApplication.exit(applicationContext, exitCodeGenerator);
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
    String accessDeniedExceptionUri() {
        return "/exception/access_denied";
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
    String jsonProcessingExceptionUri() {
        return "/exception/data/json-processing-exception";
    }





}



