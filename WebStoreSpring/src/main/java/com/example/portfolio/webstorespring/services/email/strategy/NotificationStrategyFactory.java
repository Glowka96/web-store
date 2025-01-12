package com.example.portfolio.webstorespring.services.email.strategy;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.exceptions.UnsupportedNotificationTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class NotificationStrategyFactory {

    private static final Map<NotificationType, NotificationStrategy> notificationStrategyMap = new EnumMap<>(NotificationType.class);

    public NotificationStrategyFactory(Set<NotificationStrategy> notificationStrategies) {
        createNotificationStrategyMap(notificationStrategies);
    }

    private void createNotificationStrategyMap(Set<NotificationStrategy> notificationStrategies) {
        notificationStrategies.forEach(n -> {
            notificationStrategyMap.put(n.getNotificationType(), n);
            log.debug("Putted notification type: {} and strategy: {} into map.", n.getNotificationType().name(), n.getClass().getSimpleName());
        });
    }

    public NotificationStrategy findNotificationStrategy(NotificationType notificationType) {
        log.info("Finding notification strategy for notification type: {}", notificationType.name());
        NotificationStrategy notificationStrategy = notificationStrategyMap.get(notificationType);
        if(notificationStrategy == null) {
            throw new UnsupportedNotificationTypeException(notificationType);
        }
        log.info("Returning founder notification strategy.");
        return notificationStrategy;
    }
}
