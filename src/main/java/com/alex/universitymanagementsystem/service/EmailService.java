package com.alex.universitymanagementsystem.service;


public interface EmailService {

    /**
     * Sends an email to the specified recipient with the given subject and body.
     * @param String to
     * @param String subject
     * @param String body
     */
    void sendEmail(String to, String subject, String body);

}
