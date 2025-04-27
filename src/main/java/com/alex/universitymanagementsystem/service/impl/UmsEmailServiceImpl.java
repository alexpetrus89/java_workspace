package com.alex.universitymanagementsystem.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;

import com.alex.universitymanagementsystem.service.UmsEmailService;

@Service
public class UmsEmailServiceImpl implements UmsEmailService {

    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(@NonNull String to, @NonNull String subject, @NonNull String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        javaMailSender.send(mailMessage);
    }
}
