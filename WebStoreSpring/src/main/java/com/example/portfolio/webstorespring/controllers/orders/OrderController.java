package com.example.portfolio.webstorespring.controllers.orders;

import com.example.portfolio.webstorespring.model.dto.orders.OrderRequest;
import com.example.portfolio.webstorespring.model.dto.orders.OrderResponse;
import com.example.portfolio.webstorespring.services.orders.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/accounts/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    @GetMapping()
    public ResponseEntity<List<OrderResponse>> getAllAccountOrders() {
        return ResponseEntity.ok(orderService.getAllAccountOrder());
    }

    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderByAccountIdAndOrderId(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(orderService.getAccountOrderByOrderId(orderId));
    }

    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    @PostMapping()
    public ResponseEntity<OrderResponse> saveOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.saveOrder(orderRequest));
    }

    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable("orderId") Long orderId,
                                                     @Valid @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(orderService.updateOrder(orderId, orderRequest));
    }

    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteOrderById(@PathVariable("orderId") Long orderId) {
        orderService.deleteOrderById(orderId);
        return ResponseEntity.noContent().build();
    }
}
