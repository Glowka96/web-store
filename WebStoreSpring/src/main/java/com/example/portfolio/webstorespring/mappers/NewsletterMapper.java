package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.models.dtos.subscribers.NewsletterMessageRequest;
import com.example.portfolio.webstorespring.models.entities.subscribers.NewsletterMessage;

public interface NewsletterMapper {

    static NewsletterMessage mapToEntity(NewsletterMessageRequest request) {
        return NewsletterMessage.builder()
                .message(request.message())
                .sendDate(request.sendDate())
                .build();
    }
}
