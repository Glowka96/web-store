package com.example.portfolio.webstorespring.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
    RESET_PASSWORD(15L),
    CONFIRM_EMAIL(15L),
    RECONFIRM_EMAIL(15L),
    RESTORE_EMAIL(10_800L),
    CONFIRM_NEWSLETTER(2_880L),
    RECONFIRM_NEWSLETTER(2_880L),
    CONFIRM_PRODUCT_SUBSCRIPTION(2_880L),
    RECONFIRM_PRODUCT_SUBSCRIPTION(2_880L);

    private final Long expiresMinute;

    NotificationType(Long expiresMinute) {
        this.expiresMinute = expiresMinute;
    }
}
