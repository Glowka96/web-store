package com.example.portfolio.webstorespring.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
    RESET_PASSWORD,
    CONFIRM_EMAIL,
    RECONFIRM_EMAIL,
    RESTORE_EMAIL,
    CONFIRM_NEWSLETTER,
    RECONFIRM_NEWSLETTER,
    CONFIRM_PRODUCT_SUBSCRIPTION,
    RECONFIRM_PRODUCT_SUBSCRIPTION;
}
