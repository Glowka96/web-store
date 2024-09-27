package com.example.portfolio.webstorespring.controllers.orders;

import com.example.portfolio.webstorespring.model.dto.orders.request.DeliveryTypeRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.DeliveryTypeResponse;
import com.example.portfolio.webstorespring.services.orders.DeliveryTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1")
@RequiredArgsConstructor
public class DeliveryTypeController {

    private final DeliveryTypeService deliveryTypeService;

    @GetMapping("/delivery-types")
    public ResponseEntity<List<DeliveryTypeResponse>> getAllDeliveryType() {
        return ResponseEntity.ok(deliveryTypeService.getAllDeliveryType());
    }

    @PostMapping("/admin/delivery-types")
    public ResponseEntity<DeliveryTypeResponse> saveDeliveryType(@RequestBody DeliveryTypeRequest deliveryTypeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(deliveryTypeService.saveDeliveryType(deliveryTypeRequest));
    }

    @PutMapping("/admin/delivery-types/{deliveryId}")
    public ResponseEntity<DeliveryTypeResponse> updateDeliveryType(@PathVariable(value = "deliveryId") Long deliveryId,
                                                                   @RequestBody DeliveryTypeRequest deliveryTypeRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(deliveryTypeService.updateDeliveryType(deliveryId, deliveryTypeRequest));
    }

    @DeleteMapping(value = "/admin/delivery-types/{deliveryId}")
    public ResponseEntity<Void> deleteDeliveryTypeById(@PathVariable(value = "deliveryId") Long deliveryId) {
        deliveryTypeService.deleteDeliveryType(deliveryId);
        return ResponseEntity.noContent().build();
    }
}
