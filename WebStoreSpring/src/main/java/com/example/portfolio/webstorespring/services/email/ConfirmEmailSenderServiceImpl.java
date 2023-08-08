package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.services.email.type.ConfirmEmailType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service()
@Qualifier("confirmEmailSender")
public class ConfirmEmailSenderServiceImpl extends AbstractEmailSenderService {

    public ConfirmEmailSenderServiceImpl(JavaMailSender javaMailSender) {
        super(javaMailSender);
        setEmailTypeStrategy(ConfirmEmailType.REGISTRATION);
    }
}
