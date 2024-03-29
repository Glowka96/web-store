package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.emailtypes.ResetPasswordType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service()
@Qualifier(value = "resetPasswordSender")
final class ResetPasswordSenderServiceImpl extends AbstractEmailSenderService {

    public ResetPasswordSenderServiceImpl(JavaMailSender javaMailSender) {
        super(javaMailSender, ResetPasswordType.PASSWORD);
    }
}
