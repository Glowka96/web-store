package com.example.portfolio.webstorespring.services.email.impl;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.services.email.strategy.NotificationStrategy;
import com.example.portfolio.webstorespring.services.email.strategy.NotificationStrategyFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailSenderServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private NotificationStrategyFactory notificationStrategyFactory;
    @Mock
    private NotificationStrategy notificationStrategy;
    @InjectMocks
    private EmailSenderServiceImpl underTest;

    @ParameterizedTest
    @EnumSource(value = NotificationType.class, names = {
            "RECONFIRM_EMAIL",
            "CONFIRM_EMAIL",
            "RESET_PASSWORD"
    })
    void shouldSendEmail(NotificationType notificationType) {
        String recipientEmail = "test@example.com";
        String confirmLinkWithToken = "http://example.com/confirm?token=12345";
        String emailTitle = "Example title";
        String emailMessage = "Example message: ";
        String responseMessage = "Example response";

        given(notificationStrategyFactory.findNotificationStrategy(any(NotificationType.class))).willReturn(notificationStrategy);
        given(notificationStrategy.getEmailTitle()).willReturn(emailTitle);
        given(notificationStrategy.getEmailMessage()).willReturn(emailMessage);
        given(notificationStrategy.getResponseMessage()).willReturn(responseMessage);

        Map<String, Object> resultMap = underTest.sendEmail(notificationType, recipientEmail, confirmLinkWithToken);

        ArgumentCaptor<SimpleMailMessage> simpleMailMessageArgumentCaptor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(simpleMailMessageArgumentCaptor.capture());

        SimpleMailMessage simpleMailMessage = simpleMailMessageArgumentCaptor.getValue();
        assertThat(simpleMailMessage.getTo()).containsExactly(recipientEmail);
        assertThat(simpleMailMessage.getSubject()).isEqualTo(emailTitle);
        assertThat(simpleMailMessage.getText()).isEqualTo(emailMessage + confirmLinkWithToken);
        assertThat(resultMap).containsEntry("message", responseMessage);
    }
}
