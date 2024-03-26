package com.example.portfolio.webstorespring.controllers.orders;

import com.example.portfolio.webstorespring.model.dto.orders.request.OrderRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.OrderResponse;
import com.example.portfolio.webstorespring.model.dto.orders.response.OrderResponseWithoutShipments;
import com.example.portfolio.webstorespring.services.orders.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/accounts/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping()
    public ResponseEntity<List<OrderResponseWithoutShipments>> getAllAccountOrders() {
        return ResponseEntity.ok(orderService.getAllAccountOrder());
    }

    @GetMapping("/last-five")
    public ResponseEntity<List<OrderResponseWithoutShipments>> getLastFiveAccountOrder() {
        return ResponseEntity.ok(orderService.getLastFiveAccountOrder());
    }

    @GetMapping(value = "/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @PostMapping()
    public ResponseEntity<OrderResponse> saveOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.saveOrder(orderRequest));
    }
}
