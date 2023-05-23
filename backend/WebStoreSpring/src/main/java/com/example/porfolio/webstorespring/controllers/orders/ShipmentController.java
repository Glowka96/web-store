package com.example.porfolio.webstorespring.controllers.orders;

import com.example.porfolio.webstorespring.model.dto.orders.ShipmentRequest;
import com.example.porfolio.webstorespring.model.dto.orders.ShipmentResponse;
import com.example.porfolio.webstorespring.services.orders.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/shipments")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @GetMapping()
    public ResponseEntity<List<ShipmentResponse>> getAllShipments() {
        return ResponseEntity.ok(shipmentService.getAllShipment());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponse> getShipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(shipmentService.getShipmentDtoById(id));
    }

    @PostMapping()
    public ResponseEntity<ShipmentResponse> saveShipment(@Valid @RequestBody ShipmentRequest shipmentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shipmentService.save(shipmentRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShipmentResponse> updateShipment(@PathVariable Long id,
                                                           @Valid @RequestBody ShipmentRequest shipmentRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(shipmentService.update(id, shipmentRequest));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShipmentById(@PathVariable Long id) {
        shipmentService.deleteShipmentById(id);
    }
}
