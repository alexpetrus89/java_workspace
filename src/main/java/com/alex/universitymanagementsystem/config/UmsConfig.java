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
        fieldToView.put("username", "exception/illegal/registration/invalid-username");
        fieldToView.put("usernameAlreadyTaken", "exception/illegal/registration/username-already-taken");

        // password + vincolo di classe @PasswordMatches
        fieldToView.put("password", "exception/illegal/registration/invalid-password");
        fieldToView.put("passwordsDoNotMatch", "exception/illegal/registration/password-not-match");

        fieldToView.put("firstName", "exception/illegal/registration/invalid-first-name");
        fieldToView.put("lastName", "exception/illegal/registration/invalid-last-name");
        fieldToView.put("dob", "exception/illegal/registration/invalid-dob");
        fieldToView.put("fiscalCode", "exception/illegal/registration/invalid-fiscal-code");

        // indirizzo
        final String invalidAddressView = "exception/illegal/registration/invalid-address";
        fieldToView.put("street", invalidAddressView);
        fieldToView.put("city", invalidAddressView);
        fieldToView.put("state", invalidAddressView);
        fieldToView.put("zip", invalidAddressView);

        fieldToView.put("phone", "exception/illegal/registration/invalid-phone");
        fieldToView.put("role", "exception/illegal/registration/invalid-role");

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
        return "/exception/error";
    }


    // commons uri
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



