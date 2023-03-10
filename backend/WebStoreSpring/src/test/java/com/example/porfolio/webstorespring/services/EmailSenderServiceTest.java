package com.example.porfolio.webstorespring.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class EmailSenderServiceTest {

    @Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private EmailSenderService underTest;

    @Test
    void sendEmail() {
        // given
        String email = "test@test.pl";
        String subject = "Test";
        String token = "test1234";

        String excepted = "Verify email by the link sent on your email address";

        // when
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        String actual = underTest.sendEmail(email,subject,token);

        // then
        assertThat(actual).isEqualTo(excepted);
    }
}