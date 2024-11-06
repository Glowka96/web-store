package com.example.portfolio.webstorespring.controllers.orders;

import com.example.portfolio.webstorespring.model.dto.orders.request.OrderRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.OrderResponse;
import com.example.portfolio.webstorespring.model.dto.orders.response.OrderResponseWithoutShipments;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import com.example.portfolio.webstorespring.services.orders.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/accounts/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderResponseWithoutShipments> getAllAccountOrders(@AuthenticationPrincipal AccountDetails accountDetails) {
        return orderService.getAllAccountOrder(accountDetails);
    }

    @GetMapping("/last-five")
    public List<OrderResponseWithoutShipments> getLastFiveAccountOrder(@AuthenticationPrincipal AccountDetails accountDetails) {
        return orderService.getLastFiveAccountOrder(accountDetails);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrderById(@AuthenticationPrincipal AccountDetails accountDetails,
                                      @PathVariable("orderId") Long orderId) {
        return orderService.getOrderById(accountDetails, orderId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse saveOrder(@AuthenticationPrincipal AccountDetails accountDetails,
                                   @Valid @RequestBody OrderRequest orderRequest) {
        return orderService.saveOrder(accountDetails, orderRequest);
    }
}
