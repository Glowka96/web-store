package com.example.portfolio.webstorespring.controllers.unsubscribers;

import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.services.unsubscribes.UnsubscribeNewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/unsubscribe-newsletter")
@RequiredArgsConstructor
public class UnsubscribeNewsletterController {

    private final UnsubscribeNewsletterService unsubscribeNewsletterService;

    @DeleteMapping()
    public ResponseMessageDTO unsubscribe(@RequestParam("token") String token) {
        return unsubscribeNewsletterService.deleteSubscription(token);
    }
}
