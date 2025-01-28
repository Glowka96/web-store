package com.example.portfolio.webstorespring.controllers.unsubscribers;

import com.example.portfolio.webstorespring.services.unsubscribes.UnsubscribeProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/unsubscribe-product-subscription")
@RequiredArgsConstructor
public class UnsubscribeProductController {

    private final UnsubscribeProductService unsubscribeProductService;

    @DeleteMapping()
    public Map<String, Object> unsubscribeAllSubscriptions(@RequestParam("token") String token) {
        return unsubscribeProductService.deleteAllSubscriptions(token);
    }

    @DeleteMapping("/single")
    public Map<String, Object> unsubscribeSingleProduct(@RequestParam("token") String token) {
        return unsubscribeProductService.deleteFromSingleProduct(token);
    }
}
