package com.example.portfolio.webstorespring.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
    RESET_PASSWORD("resetPassword", 15L),
    CONFIRM_EMAIL("confirmEmail", 15L),
    RECONFIRM_EMAIL("reconfirmEmail", 15L),
    RESTORE_EMAIL("restoreEmail", 10_800L);

    private final String name;
    private final Long expiresMinute;

    NotificationType(String name, Long expiresMinute) {
        this.name = name;
        this.expiresMinute = expiresMinute;
    }
}
