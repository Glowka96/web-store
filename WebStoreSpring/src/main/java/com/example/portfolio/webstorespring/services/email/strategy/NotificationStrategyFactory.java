package com.example.portfolio.webstorespring.services.email.strategy;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.exceptions.UnsupportedNotificationTypeException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Component
public class NotificationStrategyFactory {

    private static final Map<NotificationType, NotificationStrategy> notificationStrategyMap = new EnumMap<>(NotificationType.class);

    public NotificationStrategyFactory(Set<NotificationStrategy> notificationStrategies) {
        createNotificationStrategyMap(notificationStrategies);
    }

    private void createNotificationStrategyMap(Set<NotificationStrategy> notificationStrategies) {
        notificationStrategies.forEach(n -> notificationStrategyMap.put(n.getNotificationType(), n));
    }

    public NotificationStrategy findNotificationStrategy(NotificationType notificationType) {
        NotificationStrategy notificationStrategy = notificationStrategyMap.get(notificationType);
        if(notificationStrategy == null) {
            throw new UnsupportedNotificationTypeException(notificationType.getName());
        }
        return notificationStrategy;
    }
}
