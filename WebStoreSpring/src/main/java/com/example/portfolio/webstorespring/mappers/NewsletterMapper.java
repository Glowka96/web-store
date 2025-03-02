package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.models.dto.subscribers.NewsletterMessageRequest;
import com.example.portfolio.webstorespring.models.entity.subscribers.NewsletterMessage;

public interface NewsletterMapper {

    static NewsletterMessage mapToEntity(NewsletterMessageRequest request) {
        return NewsletterMessage.builder()
                .message(request.message())
                .sendDate(request.sendDate())
                .build();
    }
}
