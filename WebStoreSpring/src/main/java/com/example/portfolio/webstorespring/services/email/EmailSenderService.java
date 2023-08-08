package com.example.portfolio.webstorespring.services.email;

import java.util.Map;

public interface EmailSenderService {
     Map<String, Object> sendEmail(String email, String token);
}
