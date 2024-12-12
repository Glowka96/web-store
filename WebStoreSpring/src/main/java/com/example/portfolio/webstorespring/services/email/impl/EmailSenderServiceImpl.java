package com.example.portfolio.webstorespring.services.email.impl;

import com.example.portfolio.webstorespring.config.providers.SenderEmailProvider;
import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.services.email.EmailSenderService;
import com.example.portfolio.webstorespring.services.email.strategy.NotificationStrategy;
import com.example.portfolio.webstorespring.services.email.strategy.NotificationStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender javaMailSender;
    private final NotificationStrategyFactory notificationStrategyFactory;
    private final SenderEmailProvider senderEmailProvider;

    @Override
    @Async
    public void sendEmail(NotificationType notificationType,
                          String email,
                          String confirmLinkWithToken) {
        NotificationStrategy notificationStrategy = notificationStrategyFactory.findNotificationStrategy(notificationType);

        log.info("Sending {} email to: {}", notificationStrategy.getNotificationType().getName(), email);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(notificationStrategy.getEmailTitle());
        mailMessage.setText(notificationStrategy.getEmailMessage() + confirmLinkWithToken);
        mailMessage.setFrom(senderEmailProvider.getEmail());

        javaMailSender.send(mailMessage);
        log.info("Sent email.");
    }
}
