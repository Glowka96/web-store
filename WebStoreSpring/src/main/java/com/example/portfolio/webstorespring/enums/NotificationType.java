package com.example.portfolio.webstorespring.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
    RESET_PASSWORD("resetPassword"),
    CONFIRM_EMAIL("confirmEmail"),
    RECONFIRM_EMAIL("reconfirmEmail"),
    RESTORE_EMAIL("restoreEmail");

    private final String name;

    NotificationType(String name) {
        this.name = name;
    }
}
