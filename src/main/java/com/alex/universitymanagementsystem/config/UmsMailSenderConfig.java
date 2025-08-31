package com.alex.universitymanagementsystem.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class UmsMailSenderConfig {

    @Value("${spring.mail.password}")
    private final String password;

    @Value("${spring.mail.username}")
    private final String username;

    public UmsMailSenderConfig() {
        // get password from environment variable
        // This is a placeholder. In a real application, you would use a secure method to retrieve the password.
        password = System.getenv("GMAIL_PASSWORD");
        username = System.getenv("GMAIL_USERNAME");
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Bean
    JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(getUsername());
        mailSender.setPassword(getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    SimpleMailMessage templateSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("This is the test email template for your email:\n%s\n");
        return message;
    }

}
