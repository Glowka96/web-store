package com.example.portfolio.webstorespring.services.emails.impl;

import com.example.portfolio.webstorespring.configs.providers.SenderEmailProvider;
import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.services.emails.EmailSenderService;
import com.example.portfolio.webstorespring.services.emails.strategy.EmailStrategy;
import com.example.portfolio.webstorespring.services.emails.strategy.EmailStrategyFactory;
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
    private final EmailStrategyFactory emailStrategyFactory;
    private final SenderEmailProvider senderEmailProvider;

    @Override
    @Async
    public void sendEmail(EmailType emailType,
                          String email,
                          String ... tokensOrMessages) {
        EmailStrategy emailStrategy = emailStrategyFactory.findNotificationStrategy(emailType);

        log.info("Sending {} email to: {}", emailStrategy.getEmailType().name(), email);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(emailStrategy.getEmailTitle());
        mailMessage.setText(String.format(emailStrategy.getEmailMessage(), (Object[]) tokensOrMessages));
        mailMessage.setFrom(senderEmailProvider.getEmail());
        javaMailSender.send(mailMessage);
        log.info("Sent email.");
    }
}
