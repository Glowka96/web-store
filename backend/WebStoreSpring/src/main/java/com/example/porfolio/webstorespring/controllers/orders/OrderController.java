package com.example.porfolio.webstorespring.controllers.orders;

import com.example.porfolio.webstorespring.model.dto.orders.OrderDto;
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
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping()
    @PreAuthorize("@authServiceImpl.checkAuthorization(#accountId, #authHeader)")
    public ResponseEntity<List<OrderDto>> getAllOrdersByAccountId(@PathVariable("accountId") Long accountId,
                                                                  @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(orderService.getAllOrderDtoByAccountId(accountId));
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("@authServiceImpl.checkAuthorization(#accountId, #authHeader)")
    public ResponseEntity<OrderDto> getOrderByAccountIdAndOrderId(@PathVariable("accountId") Long accountId,
                                                                  @PathVariable("orderId") Long orderId,
                                                                  @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(orderService.getOrderByAccountIdAndOrderId(accountId, orderId));
    }

    @PostMapping()
    @PreAuthorize("@authServiceImpl.checkAuthorization(#accountId, #authHeader)")
    public ResponseEntity<OrderDto> saveOrder(@PathVariable("accountId") Long accountId,
                                              @Valid @RequestBody OrderDto orderDto,
                                              @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.saveOrder(accountId, orderDto));
    }

    @PutMapping("/{orderId}")
    @PreAuthorize("@authServiceImpl.checkAuthorization(#accountId, #authHeader)")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("accountId") Long accountId,
                                                @PathVariable("orderId") Long orderId,
                                                @Valid @RequestBody OrderDto orderDto,
                                                @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(orderService.updateOrder(accountId, orderId, orderDto));
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@authServiceImpl.checkAuthorization(#accountId, #authHeader)")
    public void deleteOrderById(@PathVariable("accountId") Long accountId,
                                @PathVariable("orderId") Long orderId,
                                @RequestHeader("Authorization") String authHeader) {
        orderService.deleteOrderById(accountId, orderId);
    }
}
