package com.example.portfolio.webstorespring.services.email.impl;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.services.email.EmailSenderService;
import com.example.portfolio.webstorespring.services.email.strategy.NotificationStrategy;
import com.example.portfolio.webstorespring.services.email.strategy.NotificationStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender javaMailSender;
    private final NotificationStrategyFactory notificationStrategyFactory;

    @Value("${sender.email}")
    private String senderEmail;

    @Override
    public Map<String, Object> sendEmail(NotificationType notificationType,
                                         String email,
                                         String confirmLinkWithToken) {
        NotificationStrategy notificationStrategy = notificationStrategyFactory.findNotificationStrategy(notificationType);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(notificationStrategy.getEmailTitle());
        mailMessage.setText(notificationStrategy.getEmailMessage() + confirmLinkWithToken);
        mailMessage.setFrom(senderEmail);

        javaMailSender.send(mailMessage);

        return Map.of("message", notificationStrategy.getResponseMessage());
    }
}
