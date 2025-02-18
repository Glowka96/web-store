package com.example.portfolio.webstorespring.services.emails;

import com.example.portfolio.webstorespring.enums.EmailType;

public interface EmailSenderService {
    void sendEmail(EmailType emailType,
                   String email,
                   String ... tokensOrMessages);
}
