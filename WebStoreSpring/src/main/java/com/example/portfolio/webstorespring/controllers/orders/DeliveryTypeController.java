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
    public ResponseEntity<List<DeliveryTypeResponse>> getAllDeliveryType(){
        return ResponseEntity.ok(deliveryTypeService.getAllDeliveryType());
    }

    @PostMapping("/admin/delivery-types")
    public ResponseEntity<DeliveryTypeResponse> saveDelivery(@RequestBody DeliveryTypeRequest deliveryTypeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(deliveryTypeService.saveDeliveryType(deliveryTypeRequest));
    }

    @DeleteMapping(value = "/admin/delivery-types/{deliveryId}")
    public ResponseEntity<Void> deleteDeliveryTypeById(@PathVariable(value = "deliveryId") Long deliveryId){
        deliveryTypeService.deleteDeliveryType(deliveryId);
        return ResponseEntity.noContent().build();
    }
}
