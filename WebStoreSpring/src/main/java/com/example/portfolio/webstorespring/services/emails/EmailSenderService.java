package com.example.portfolio.webstorespring.services.emails;

import com.example.portfolio.webstorespring.enums.NotificationType;

public interface EmailSenderService {
    void sendEmail(NotificationType notificationType,
                   String email,
                   String confirmLinkWithToken);
}
