package com.example.portfolio.webstorespring.services.emails.impl;

import com.example.portfolio.webstorespring.configs.providers.SenderEmailProvider;
import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.services.emails.strategy.EmailStrategy;
import com.example.portfolio.webstorespring.services.emails.strategy.EmailStrategyFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailSenderServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private EmailStrategyFactory emailStrategyFactory;
    @Mock
    private EmailStrategy emailStrategy;
    @Mock
    private SenderEmailProvider senderEmailProvider;
    @InjectMocks
    private EmailSenderServiceImpl underTest;

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
    void shouldSendEmail(EmailType emailType) {
        String recipientEmail = "test@example.com";
        String confirmLinkWithToken = "http://example.com/confirm?token=12345";
        String emailTitle = "Example title";
        String emailMessage = "Example message: %s";

        given(emailStrategyFactory.findNotificationStrategy(any(EmailType.class))).willReturn(emailStrategy);
        given(emailStrategy.getEmailTitle()).willReturn(emailTitle);
        given(emailStrategy.getEmailMessage()).willReturn(emailMessage);
        given(senderEmailProvider.getEmail()).willReturn("sender@email.com");
        given(emailStrategy.getNotificationType()).willReturn(emailType);

        underTest.sendEmail(emailType, recipientEmail, confirmLinkWithToken);

        ArgumentCaptor<SimpleMailMessage> simpleMailMessageArgumentCaptor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(simpleMailMessageArgumentCaptor.capture());

        SimpleMailMessage simpleMailMessage = simpleMailMessageArgumentCaptor.getValue();
        assertArrayEquals(new String[]{recipientEmail}, simpleMailMessage.getTo());
        assertEquals(emailTitle, simpleMailMessage.getSubject());
        assertEquals(String.format(emailMessage, confirmLinkWithToken), simpleMailMessage.getText());
    }
}
