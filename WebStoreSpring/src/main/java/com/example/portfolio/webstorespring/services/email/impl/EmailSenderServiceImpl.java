package com.example.portfolio.webstorespring.services.email.impl;

import com.example.portfolio.webstorespring.controllers.emails.strategy.NotificationStrategy;
import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.services.email.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender javaMailSender;
    private final Map<String, NotificationStrategy> notificationTypes = new HashMap<>();

    @Value("${sender.email}")
    private String senderEmail;

    @Override
    public Map<String, Object> sendEmail(NotificationType notificationType,
                                         String email,
                                         String confirmLinkWithToken) {
        NotificationStrategy notificationStrategy = notificationTypes.get(notificationType.getName());
        if (notificationStrategy == null) {
            throw new UnsupportedOperationException();
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(notificationStrategy.getEmailTitle());
        mailMessage.setText(notificationStrategy.getEmailMessage() + confirmLinkWithToken);
        mailMessage.setFrom(senderEmail);

        javaMailSender.send(mailMessage);

        return Map.of("message", notificationStrategy.getResponseMessage());
    }

    @Override
    public void registerNotification(NotificationType notificationType, NotificationStrategy notificationStrategy) {
        notificationTypes.put(notificationType.getName(), notificationStrategy);
    }
}
