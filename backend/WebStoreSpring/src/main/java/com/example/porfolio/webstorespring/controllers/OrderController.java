package com.example.porfolio.webstorespring.controllers;

import com.example.porfolio.webstorespring.exceptions.OrderCanNotBeUpdated;
import com.example.porfolio.webstorespring.model.dto.orders.OrderDto;
import com.example.porfolio.webstorespring.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrderDto());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.getOrderDtoById(id));
    }

    @PostMapping("/accounts/{accountId}/orders")
    public ResponseEntity<OrderDto> saveOrder(@PathVariable("accountId") Long accountId,
                                              @Valid @RequestBody OrderDto orderDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(accountId, orderDto));
    }

    @PutMapping("/accounts/{accountId}/orders/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("accountId") Long accountId,
                                                @PathVariable("orderId") Long orderId,
                                                @Valid @RequestBody OrderDto orderDto) throws OrderCanNotBeUpdated {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(orderService.update(accountId, orderId, orderDto));
    }

    @DeleteMapping("/orders/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrderById(@PathVariable("orderId") Long orderId) {
        orderService.deleteOrderById(orderId);
    }
}
