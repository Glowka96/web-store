package com.example.portfolio.webstorespring.services.email.type;

public interface EmailTypeStrategy {
    String getTitle();
    String getEmailMessage();
    String getInformationMessage();
}
