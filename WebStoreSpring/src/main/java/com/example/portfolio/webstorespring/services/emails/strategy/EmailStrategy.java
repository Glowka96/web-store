package com.example.portfolio.webstorespring.services.emails.strategy;

import com.example.portfolio.webstorespring.enums.EmailType;

public interface EmailStrategy {

    EmailType getNotificationType();

    String getEmailTitle();

    String getEmailMessage();
}
