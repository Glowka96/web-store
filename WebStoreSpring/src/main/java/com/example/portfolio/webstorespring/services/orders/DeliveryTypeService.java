package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.DeliveryTypeMapper;
import com.example.portfolio.webstorespring.models.dto.orders.request.DeliveryTypeRequest;
import com.example.portfolio.webstorespring.models.dto.orders.response.DeliveryTypeResponse;
import com.example.portfolio.webstorespring.models.entity.orders.DeliveryType;
import com.example.portfolio.webstorespring.repositories.orders.DeliveryTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryTypeService {

    private final DeliveryTypeRepository deliveryTypeRepository;

    public List<DeliveryTypeResponse> getAll() {
        log.info("Fetching all delivery type.");
        return DeliveryTypeMapper.mapToDto(deliveryTypeRepository.findAll());
    }

    public DeliveryTypeResponse save(DeliveryTypeRequest request){
        log.info("Mapping delivery type request: {}", request);
        DeliveryType deliveryType = DeliveryTypeMapper.mapToEntity(request);

        deliveryTypeRepository.save(deliveryType);
        log.info("Saved delivery type.");
        return DeliveryTypeMapper.mapToDto(deliveryType);
    }

    public DeliveryTypeResponse update(Long id, DeliveryTypeRequest request) {
        log.info("Finding delivery type with ID: {}", id);
        DeliveryType foundDeliveryType = findById(id);

        log.debug("Updating delivery type fields for ID: {}", id);
        foundDeliveryType.setName(request.name());
        foundDeliveryType.setPrice(request.price());
        deliveryTypeRepository.save(foundDeliveryType);
        log.info("Updated delivery type.");
        return DeliveryTypeMapper.mapToDto(foundDeliveryType);
    }

    public void deleteById(Long id){
        log.info("Deleting delivery type for ID: {}", id);
        deliveryTypeRepository.deleteById(id);
    }

    DeliveryType findById(Long id) {
        return deliveryTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery type", "id", id));
    }
}
