package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.services.email.type.ConfirmEmailType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class ConfirmEmailSenderServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private ConfirmEmailSenderServiceImpl confirmedEmail;

    @Test
    void sendEmail() {
        // given
        String email = "test@test.pl";
        String token = "test1234";

        Map<String, Object> excepted = new HashMap<>();
        excepted.put("message", ConfirmEmailType.REGISTRATION.getMessage());

        // when
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        boolean actual = confirmedEmail.sendEmail(email, token)
                .values()
                .stream().findFirst()
                .filter((message) -> message.equals(excepted.get("message")))
                .isPresent();

        // then
        assertThat(actual).isEqualTo(true);
    }
}