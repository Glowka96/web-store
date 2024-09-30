package com.example.portfolio.webstorespring.services.email.strategy;

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
    private NotificationStrategy reconfirmEmailStrategy;
    @Mock
    private NotificationStrategy confirmEmailStrategy;
    @Mock
    private NotificationStrategy resetPasswordStrategy;

    @BeforeEach
    void setUp() {
        when(reconfirmEmailStrategy.getNotificationType()).thenReturn(NotificationType.RECONFIRM_EMAIL);
        when(confirmEmailStrategy.getNotificationType()).thenReturn(NotificationType.CONFIRM_EMAIL);
        when(resetPasswordStrategy.getNotificationType()).thenReturn(NotificationType.RESET_PASSWORD);

        Set<NotificationStrategy> strategies = Set.of(reconfirmEmailStrategy, confirmEmailStrategy, resetPasswordStrategy);

        underTest = new NotificationStrategyFactory(strategies);
    }

    @ParameterizedTest
    @EnumSource(value = NotificationType.class, names = {
            "RECONFIRM_EMAIL",
            "CONFIRM_EMAIL",
            "RESET_PASSWORD"
    })
    void shouldFindNotificationStrategy_whenNotificationTypeIsGiven(NotificationType notificationType) {
        NotificationStrategy notificationStrategy = underTest.findNotificationStrategy(notificationType);

        assertNotNull(notificationStrategy);
    }
}
