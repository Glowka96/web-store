package com.example.portfolio.webstorespring.controllers.orders;

import com.example.portfolio.webstorespring.models.dtos.orders.requests.DeliveryTypeRequest;
import com.example.portfolio.webstorespring.models.dtos.orders.responses.DeliveryTypeResponse;
import com.example.portfolio.webstorespring.services.orders.DeliveryTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1")
@RequiredArgsConstructor
public class DeliveryTypeController {

    private final DeliveryTypeService deliveryTypeService;

    @GetMapping("/delivery-types")
    public List<DeliveryTypeResponse> getAllDeliveryType() {
        return deliveryTypeService.getAll();
    }

    @PostMapping("/admin/delivery-types")
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryTypeResponse saveDeliveryType(@RequestBody DeliveryTypeRequest request) {
        return deliveryTypeService.save(request);
    }

    @PutMapping("/admin/delivery-types/{deliveryId}")
    public DeliveryTypeResponse updateDeliveryType(@PathVariable("deliveryId") Long deliveryId,
                                                   @RequestBody DeliveryTypeRequest request) {
        return deliveryTypeService.update(deliveryId, request);
    }

    @DeleteMapping("/admin/delivery-types/{deliveryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeliveryTypeById(@PathVariable("deliveryId") Long deliveryId) {
        deliveryTypeService.deleteById(deliveryId);
    }
}
