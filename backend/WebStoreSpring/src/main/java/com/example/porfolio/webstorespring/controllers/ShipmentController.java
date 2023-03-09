package com.example.porfolio.webstorespring.controllers;

import com.example.porfolio.webstorespring.model.dto.orders.ShipmentDto;
import com.example.porfolio.webstorespring.services.ShipmentService;
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
    public ResponseEntity<List<ShipmentDto>> getAllShipments() {
        return ResponseEntity.ok(shipmentService.getAllShipment());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentDto> getShipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(shipmentService.getShipmentDtoById(id));
    }

    @PostMapping()
    public ResponseEntity<ShipmentDto> saveShipment(@Valid @RequestBody ShipmentDto shipmentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shipmentService.save(shipmentDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShipmentDto> updateShipment(@PathVariable Long id,
                                                      @Valid @RequestBody ShipmentDto shipmentDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(shipmentService.update(id, shipmentDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShipmentById(@PathVariable Long id) {
        shipmentService.deleteShipmentById(id);
    }
}
