package com.example.portfolio.webstorespring.services.tokens.confirmations;

import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.exceptions.UnsupportedNotificationTypeException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationExpirationManager {

    private static final Map<EmailType, Long> expirationMinutes = Map.of(
            EmailType.RESET_PASSWORD, 15L,
            EmailType.CONFIRM_EMAIL, 15L,
            EmailType.RECONFIRM_EMAIL, 15L,
            EmailType.RESTORE_EMAIL, 10_800L,
            EmailType.CONFIRM_NEWSLETTER, 2_880L,
            EmailType.RECONFIRM_NEWSLETTER, 2_880L,
            EmailType.CONFIRM_PRODUCT_SUBSCRIPTION, 2_880L,
            EmailType.RECONFIRM_PRODUCT_SUBSCRIPTION, 2_880L
    );

    Long getExpirationMinutes(EmailType type) {
        Long expiresMinute = expirationMinutes.get(type);
        if(expiresMinute == null) {
            throw new UnsupportedNotificationTypeException(type);
        }
        return expiresMinute;
    }
}
