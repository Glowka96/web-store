package com.example.portfolio.webstorespring.services.emails.strategy;

import com.example.portfolio.webstorespring.enums.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationStrategyFactoryTest {

    private NotificationStrategyFactory underTest;
    @Mock
    private NotificationStrategy resetPasswordStrategy;
    @Mock
    private NotificationStrategy confirmEmailStrategy;
    @Mock
    private NotificationStrategy reconfirmEmailStrategy;
    @Mock
    private NotificationStrategy restoreEmailStrategy;
    @Mock
    private NotificationStrategy confirmNewsletterEmailStrategy;
    @Mock
    private NotificationStrategy reconfirmNewsletterEmailStrategy;
    @Mock
    private NotificationStrategy welcomeNewsletterEmailStrategy;
    @Mock
    private NotificationStrategy confirmProfileSubscriptionEmailStrategy;
    @Mock
    private NotificationStrategy reconfirmProfileSubscriptionEmailStrategy;
    @Mock
    private NotificationStrategy welcomeProductSubscriptionEmailStrategy;


    @ParameterizedTest
    @EnumSource(value = NotificationType.class, names = {
            "RESET_PASSWORD",
            "CONFIRM_EMAIL",
            "RECONFIRM_EMAIL",
            "CONFIRM_NEWSLETTER",
            "RECONFIRM_NEWSLETTER",
            "WELCOME_NEWSLETTER",
            "CONFIRM_PRODUCT_SUBSCRIPTION",
            "RECONFIRM_PRODUCT_SUBSCRIPTION",
            "WELCOME_PRODUCT_SUBSCRIPTION",
    })
    void shouldFindNotificationStrategy_whenNotificationTypeIsGiven(NotificationType notificationType) {
        NotificationStrategy notificationStrategy = underTest.findNotificationStrategy(notificationType);

        assertNotNull(notificationStrategy);
    }

    @BeforeEach
    void setUp() {
        when(resetPasswordStrategy.getNotificationType()).thenReturn(NotificationType.RESET_PASSWORD);
        when(confirmEmailStrategy.getNotificationType()).thenReturn(NotificationType.CONFIRM_EMAIL);
        when(reconfirmEmailStrategy.getNotificationType()).thenReturn(NotificationType.RECONFIRM_EMAIL);
        when(restoreEmailStrategy.getNotificationType()).thenReturn(NotificationType.RESTORE_EMAIL);
        when(confirmNewsletterEmailStrategy.getNotificationType()).thenReturn(NotificationType.CONFIRM_NEWSLETTER);
        when(reconfirmNewsletterEmailStrategy.getNotificationType()).thenReturn(NotificationType.RECONFIRM_NEWSLETTER);
        when(welcomeNewsletterEmailStrategy.getNotificationType()).thenReturn(NotificationType.WELCOME_NEWSLETTER);
        when(confirmProfileSubscriptionEmailStrategy.getNotificationType()).thenReturn(NotificationType.CONFIRM_PRODUCT_SUBSCRIPTION);
        when(reconfirmProfileSubscriptionEmailStrategy.getNotificationType()).thenReturn(NotificationType.RECONFIRM_PRODUCT_SUBSCRIPTION);
        when(welcomeProductSubscriptionEmailStrategy.getNotificationType()).thenReturn(NotificationType.WELCOME_PRODUCT_SUBSCRIPTION);

        Set<NotificationStrategy> strategies = Set.of(
                resetPasswordStrategy,
                confirmEmailStrategy,
                reconfirmEmailStrategy,
                restoreEmailStrategy,
                confirmNewsletterEmailStrategy,
                reconfirmNewsletterEmailStrategy,
                welcomeNewsletterEmailStrategy,
                confirmProfileSubscriptionEmailStrategy,
                reconfirmProfileSubscriptionEmailStrategy,
                welcomeProductSubscriptionEmailStrategy
                );

        underTest = new NotificationStrategyFactory(strategies);
    }
}
