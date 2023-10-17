package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.emailtypes.ResetPasswordType;
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
class ResetPasswordSenderServiceImplTest {
    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private ResetPasswordSenderServiceImpl underTest;

    @Test
    void sendEmail() {
        // given
        String email = "test@test.pl";
        String token = "test1234";

        Map<String, Object> excepted = new HashMap<>();
        excepted.put("message", ResetPasswordType.PASSWORD.getInformationMessage());

        // when
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        boolean actual = underTest.sendEmail(email, token)
                .values()
                .stream().findFirst()
                .filter((message) -> message.equals(excepted.get("message")))
                .isPresent();

        // then
        assertThat(actual).isTrue();
    }
}
