package com.example.portfolio.webstorespring.services.emails.strategy;

import com.example.portfolio.webstorespring.enums.EmailType;
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
class EmailStrategyFactoryTest {

    private EmailStrategyFactory underTest;
    @Mock
    private EmailStrategy resetPasswordStrategy;
    @Mock
    private EmailStrategy confirmEmailStrategy;
    @Mock
    private EmailStrategy reconfirmEmailStrategy;
    @Mock
    private EmailStrategy restoreEmailStrategy;
    @Mock
    private EmailStrategy confirmNewsletterEmailStrategy;
    @Mock
    private EmailStrategy reconfirmNewsletterEmailStrategy;
    @Mock
    private EmailStrategy welcomeNewsletterEmailStrategy;
    @Mock
    private EmailStrategy confirmProfileSubscriptionEmailStrategy;
    @Mock
    private EmailStrategy reconfirmProfileSubscriptionEmailStrategy;
    @Mock
    private EmailStrategy welcomeProductSubscriptionEmailStrategy;


    @ParameterizedTest
    @EnumSource(value = EmailType.class, names = {
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
    void shouldFindNotificationStrategy_whenNotificationTypeIsGiven(EmailType emailType) {
        EmailStrategy emailStrategy = underTest.findNotificationStrategy(emailType);

        assertNotNull(emailStrategy);
    }

    @BeforeEach
    void setUp() {
        when(resetPasswordStrategy.getEmailType()).thenReturn(EmailType.RESET_PASSWORD);
        when(confirmEmailStrategy.getEmailType()).thenReturn(EmailType.CONFIRM_EMAIL);
        when(reconfirmEmailStrategy.getEmailType()).thenReturn(EmailType.RECONFIRM_EMAIL);
        when(restoreEmailStrategy.getEmailType()).thenReturn(EmailType.RESTORE_EMAIL);
        when(confirmNewsletterEmailStrategy.getEmailType()).thenReturn(EmailType.CONFIRM_NEWSLETTER);
        when(reconfirmNewsletterEmailStrategy.getEmailType()).thenReturn(EmailType.RECONFIRM_NEWSLETTER);
        when(welcomeNewsletterEmailStrategy.getEmailType()).thenReturn(EmailType.WELCOME_NEWSLETTER);
        when(confirmProfileSubscriptionEmailStrategy.getEmailType()).thenReturn(EmailType.CONFIRM_PRODUCT_SUBSCRIPTION);
        when(reconfirmProfileSubscriptionEmailStrategy.getEmailType()).thenReturn(EmailType.RECONFIRM_PRODUCT_SUBSCRIPTION);
        when(welcomeProductSubscriptionEmailStrategy.getEmailType()).thenReturn(EmailType.WELCOME_PRODUCT_SUBSCRIPTION);

        Set<EmailStrategy> strategies = Set.of(
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

        underTest = new EmailStrategyFactory(strategies);
    }
}
