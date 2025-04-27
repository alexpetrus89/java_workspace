package com.alex.universitymanagementsystem.service;

import org.springframework.lang.NonNull;

public interface UmsEmailService {

    /**
     * Sends an email to the specified recipient with the given subject and body.
     * @param String to
     * @param String subject
     * @param String body
     * @throws NullPointerException if any of the parameters are null
     */
    void sendEmail(@NonNull String to, @NonNull String subject, @NonNull String body)
        throws NullPointerException;

}
