package com.example.portfolio.webstorespring.services.tokens.confirmations;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.exceptions.UnsupportedNotificationTypeException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationExpirationManager {

    private static final Map<NotificationType, Long> expirationMinutes = Map.of(
            NotificationType.RESET_PASSWORD, 15L,
            NotificationType.CONFIRM_EMAIL, 15L,
            NotificationType.RECONFIRM_EMAIL, 15L,
            NotificationType.RESTORE_EMAIL, 10_800L,
            NotificationType.CONFIRM_NEWSLETTER, 2_880L,
            NotificationType.RECONFIRM_NEWSLETTER, 2_880L,
            NotificationType.CONFIRM_PRODUCT_SUBSCRIPTION, 2_880L,
            NotificationType.RECONFIRM_PRODUCT_SUBSCRIPTION, 2_880L
    );

    Long getExpirationMinutes(NotificationType type) {
        Long expiresMinute = expirationMinutes.get(type);
        if(expiresMinute == null) {
            throw new UnsupportedNotificationTypeException(type);
        }
        return expiresMinute;
    }
}
