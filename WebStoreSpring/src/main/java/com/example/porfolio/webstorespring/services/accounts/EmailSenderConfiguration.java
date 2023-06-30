package com.example.porfolio.webstorespring.services.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class EmailSenderConfiguration {

    private final JavaMailSender javaMailSender;

    public Map<String, Object> sendEmail(String email, String subject, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:4200/registration/confirm?token=" + token);
        mailMessage.setFrom("glowackisebastian.it@gmail.com");
        javaMailSender.send(mailMessage);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Verify email by the link sent on your email address");
        return responseBody;
    }
}
