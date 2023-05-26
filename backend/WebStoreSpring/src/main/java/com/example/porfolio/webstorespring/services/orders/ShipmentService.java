package com.example.porfolio.webstorespring.services.orders;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.ShipmentMapper;
import com.example.porfolio.webstorespring.model.dto.orders.ShipmentRequest;
import com.example.porfolio.webstorespring.model.dto.orders.ShipmentResponse;
import com.example.porfolio.webstorespring.model.entity.orders.Shipment;
import com.example.porfolio.webstorespring.repositories.orders.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;

    public ShipmentResponse getShipmentDtoById(Long id) {
        Shipment shipment = findShipmentById(id);
        return shipmentMapper.mapToDto(shipment);
    }

    public List<ShipmentResponse> getAllShipment() {
        return shipmentMapper.mapToDto(shipmentRepository.findAll());
    }

    public ShipmentResponse save(ShipmentRequest shipmentRequest) {
        Shipment shipment = shipmentMapper.mapToEntity(shipmentRequest);

        shipment.setPrice(BigDecimal.valueOf(
                shipment.getProduct().getPrice() * shipment.getQuantity()
        ).doubleValue());

        shipmentRepository.save(shipment);
        return shipmentMapper.mapToDto(shipment);
    }

    public ShipmentResponse update(Long shipmentId, ShipmentRequest shipmentRequest) {
        Shipment foundShipment = findShipmentById(shipmentId);
        Shipment shipment = shipmentMapper.mapToEntity(shipmentRequest);

        shipment.setId(foundShipment.getId());

        shipmentRepository.save(shipment);
        return shipmentMapper.mapToDto(shipment);
    }

    public void deleteShipmentById(Long id) {
        Shipment foundShipment = findShipmentById(id);
        shipmentRepository.delete(foundShipment);
    }

    private Shipment findShipmentById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment", "id", id));
    }
}
