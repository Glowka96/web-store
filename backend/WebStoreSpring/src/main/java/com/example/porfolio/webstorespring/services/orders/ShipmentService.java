package com.example.porfolio.webstorespring.services.orders;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.ShipmentMapper;
import com.example.porfolio.webstorespring.model.dto.orders.ShipmentDto;
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

    public ShipmentDto getShipmentDtoById(Long id) {
        Shipment shipment = findShipmentById(id);
        return shipmentMapper.mapToDto(shipment);
    }

    public List<ShipmentDto> getAllShipment() {
        return shipmentMapper.mapToDto(shipmentRepository.findAll());
    }

    public ShipmentDto save(ShipmentDto shipmentDto) {
        Shipment shipment = shipmentMapper.mapToEntity(shipmentDto);

        BigDecimal price = BigDecimal.valueOf(
                shipment.getProduct().getPrice() * shipment.getQuantity());
        shipment.setPrice(price.doubleValue());

        shipmentRepository.save(shipment);
        return shipmentMapper.mapToDto(shipment);
    }

    public ShipmentDto update(Long shipmentId, ShipmentDto shipmentDto) {
        Shipment foundShipment = findShipmentById(shipmentId);
        Shipment shipment = shipmentMapper.mapToEntity(shipmentDto);

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
