package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.emailtypes.EmailTypeStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Map;

@RequiredArgsConstructor
public abstract class AbstractEmailSenderService implements EmailSenderService {

    private final JavaMailSender javaMailSender;
    private final EmailTypeStrategy emailTypeStrategy;

    @Value("${sender.email}")
    private String senderEmail;

    @Override
    public Map<String, Object> sendEmail(String email, String confirmLinkWithToken) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(emailTypeStrategy.getTitle());
        mailMessage.setText(emailTypeStrategy.getEmailMessage() + confirmLinkWithToken);
        mailMessage.setFrom(senderEmail);

        javaMailSender.send(mailMessage);

        return Map.of("message", emailTypeStrategy.getInformationMessage());
    }
}
