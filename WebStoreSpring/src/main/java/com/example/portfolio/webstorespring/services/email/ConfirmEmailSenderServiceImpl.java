package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.emailtypes.ConfirmEmailType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service()
@Qualifier(value = "confirmEmailSender")
final class ConfirmEmailSenderServiceImpl extends AbstractEmailSenderService {

    public ConfirmEmailSenderServiceImpl(JavaMailSender javaMailSender) {
        super(javaMailSender, ConfirmEmailType.REGISTRATION);
    }
}
