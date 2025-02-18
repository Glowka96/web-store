package com.example.portfolio.webstorespring.services.tokens.confirmations;

import com.example.portfolio.webstorespring.enums.EmailType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class NotificationExpirationManagerTest {

    private NotificationExpirationManager expirationManager;

    @BeforeEach
    void setUp() {
        expirationManager = new NotificationExpirationManager();
    }

    @Test
    void shouldReturnCorrectExpirationMinutes_forNotificationTypes() {
        assertThat(expirationManager.getExpirationMinutes(EmailType.RESET_PASSWORD)).isEqualTo(15L);
        assertThat(expirationManager.getExpirationMinutes(EmailType.CONFIRM_EMAIL)).isEqualTo(15L);
        assertThat(expirationManager.getExpirationMinutes(EmailType.RECONFIRM_EMAIL)).isEqualTo(15L);
        assertThat(expirationManager.getExpirationMinutes(EmailType.RESTORE_EMAIL)).isEqualTo(10_800L);
        assertThat(expirationManager.getExpirationMinutes(EmailType.CONFIRM_NEWSLETTER)).isEqualTo(2_880L);
        assertThat(expirationManager.getExpirationMinutes(EmailType.RECONFIRM_NEWSLETTER)).isEqualTo(2_880L);
        assertThat(expirationManager.getExpirationMinutes(EmailType.CONFIRM_PRODUCT_SUBSCRIPTION)).isEqualTo(2_880L);
        assertThat(expirationManager.getExpirationMinutes(EmailType.RECONFIRM_PRODUCT_SUBSCRIPTION)).isEqualTo(2_880L);
    }
}