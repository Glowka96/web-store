package com.example.porfolio.webstorespring.controllers.orders;

import com.example.porfolio.webstorespring.model.dto.orders.OrderRequest;
import com.example.porfolio.webstorespring.model.dto.orders.OrderResponse;
import com.example.porfolio.webstorespring.services.orders.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/accounts/{accountId}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping()
    @PreAuthorize("@authServiceImpl.checkAuthorization(#accountId, #authHeader)")
    public ResponseEntity<List<OrderResponse>> getAllOrdersByAccountId(@PathVariable("accountId") Long accountId,
                                                                       @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(orderService.getAllOrderDtoByAccountId(accountId));
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("@authServiceImpl.checkAuthorization(#accountId, #authHeader)")
    public ResponseEntity<OrderResponse> getOrderByAccountIdAndOrderId(@PathVariable("accountId") Long accountId,
                                                                       @PathVariable("orderId") Long orderId,
                                                                       @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(orderService.getOrderByAccountIdAndOrderId(accountId, orderId));
    }

    @PostMapping()
    @PreAuthorize("@authServiceImpl.checkAuthorization(#accountId, #authHeader)")
    public ResponseEntity<OrderResponse> saveOrder(@PathVariable("accountId") Long accountId,
                                                   @Valid @RequestBody OrderRequest orderRequest,
                                                   @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.saveOrder(accountId, orderRequest));
    }

    @PutMapping("/{orderId}")
    @PreAuthorize("@authServiceImpl.checkAuthorization(#accountId, #authHeader)")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable("accountId") Long accountId,
                                                     @PathVariable("orderId") Long orderId,
                                                     @Valid @RequestBody OrderRequest orderRequest,
                                                     @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(orderService.updateOrder(accountId, orderId, orderRequest));
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@authServiceImpl.checkAuthorization(#accountId, #authHeader)")
    public void deleteOrderById(@PathVariable("accountId") Long accountId,
                                @PathVariable("orderId") Long orderId,
                                @RequestHeader("Authorization") String authHeader) {
        orderService.deleteOrderById(orderId);
    }
}
