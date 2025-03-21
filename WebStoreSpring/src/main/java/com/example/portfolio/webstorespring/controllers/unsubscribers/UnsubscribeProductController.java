package com.example.portfolio.webstorespring.controllers.unsubscribers;

import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.services.unsubscribes.UnsubscribeProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/unsubscribe-product-subscriptions")
@RequiredArgsConstructor
public class UnsubscribeProductController {

    private final UnsubscribeProductService unsubscribeProductService;

    @DeleteMapping("/confirm")
    public ResponseMessageDTO unsubscribeAllSubscriptions(@RequestParam("token") String token) {
        return unsubscribeProductService.deleteAllSubscriptions(token);
    }

    @DeleteMapping("/single/confirm")
    public ResponseMessageDTO unsubscribeSingleProduct(@RequestParam("token") String token) {
        return unsubscribeProductService.deleteFromSingleProduct(token);
    }
}
