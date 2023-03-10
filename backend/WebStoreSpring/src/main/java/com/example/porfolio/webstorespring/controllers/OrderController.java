package com.example.porfolio.webstorespring.controllers;

import com.example.porfolio.webstorespring.model.dto.orders.OrderDto;
import com.example.porfolio.webstorespring.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/accounts/{accountId}/orders")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping()
    public ResponseEntity<List<OrderDto>> getAllOrdersByAccountId(@PathVariable("accountId") Long accountId) {
        return ResponseEntity.ok(orderService.getAllOrderDtoByAccountId(accountId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderByAccountIdAndOrderId(@PathVariable("accountId") Long accountId,
                                                                  @PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(orderService.getOrderByAccountIdAndOrderId(accountId, orderId));
    }

    @PostMapping()
    public ResponseEntity<OrderDto> saveOrder(@PathVariable("accountId") Long accountId,
                                              @Valid @RequestBody OrderDto orderDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.saveOrder(accountId, orderDto));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("accountId") Long accountId,
                                                @PathVariable("orderId") Long orderId,
                                                @Valid @RequestBody OrderDto orderDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(orderService.updateOrder(accountId, orderId, orderDto));
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrderById(@PathVariable("accountId") Long accountId,
                                @PathVariable("orderId") Long orderId) {
        orderService.deleteOrderById(accountId, orderId);
    }
}
