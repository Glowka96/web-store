package com.example.portfolio.webstorespring.enums.emailtypes;

public interface EmailTypeStrategy {
    String getTitle();
    String getEmailMessage();
    String getInformationMessage();
}
