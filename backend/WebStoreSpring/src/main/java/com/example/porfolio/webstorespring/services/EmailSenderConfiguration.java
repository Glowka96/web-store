package com.example.porfolio.webstorespring.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
@RequiredArgsConstructor
public class EmailSenderConfiguration {

    private final JavaMailSender javaMailSender;

    public String sendEmail(String email, String subject, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:8080/api/v1/registration/confirm?token=" + token);
        mailMessage.setFrom("glowackispring@gmail.com");
        javaMailSender.send(mailMessage);

        return "Verify email by the link sent on your email address";
    }
}
