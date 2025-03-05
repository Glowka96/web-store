package com.example.portfolio.webstorespring.services.emails.strategy;

import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.exceptions.UnsupportedEmailTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class EmailStrategyFactory {

    private static final Map<EmailType, EmailStrategy> notificationStrategyMap = new EnumMap<>(EmailType.class);

    public EmailStrategyFactory(Set<EmailStrategy> notificationStrategies) {
        createNotificationStrategyMap(notificationStrategies);
    }

    private void createNotificationStrategyMap(Set<EmailStrategy> notificationStrategies) {
        notificationStrategies.forEach(n -> {
            notificationStrategyMap.put(n.getEmailType(), n);
            log.debug("Putted notification type: {} and strategy: {} into map.", n.getEmailType().name(), n.getClass().getSimpleName());
        });
    }

    public EmailStrategy findNotificationStrategy(EmailType emailType) {
        log.info("Finding notification strategy for notification type: {}", emailType.name());
        EmailStrategy emailStrategy = notificationStrategyMap.get(emailType);
        if(emailStrategy == null) {
            throw new UnsupportedEmailTypeException(emailType);
        }
        log.info("Returning founder notification strategy.");
        return emailStrategy;
    }
}
