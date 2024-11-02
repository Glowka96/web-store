package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.DeliveryTypeMapper;
import com.example.portfolio.webstorespring.model.dto.orders.request.DeliveryTypeRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.DeliveryTypeResponse;
import com.example.portfolio.webstorespring.model.entity.orders.DeliveryType;
import com.example.portfolio.webstorespring.repositories.orders.DeliveryTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryTypeService {

    private final DeliveryTypeRepository deliveryTypeRepository;
    private final DeliveryTypeMapper deliveryTypeMapper;

    public List<DeliveryTypeResponse> getAllDeliveryType() {
        return deliveryTypeMapper.mapToDto(deliveryTypeRepository.findAll());
    }

    public DeliveryTypeResponse saveDeliveryType(DeliveryTypeRequest deliveryTypeRequest){
        DeliveryType deliveryType = deliveryTypeMapper.mapToEntity(deliveryTypeRequest);

        deliveryTypeRepository.save(deliveryType);
        return deliveryTypeMapper.mapToDto(deliveryType);
    }

    public DeliveryTypeResponse updateDeliveryType(Long deliveryId, DeliveryTypeRequest deliveryTypeRequest) {
        DeliveryType foundDeliveryType = findDeliveryTypeById(deliveryId);

        foundDeliveryType.setName(deliveryTypeRequest.getName());
        foundDeliveryType.setPrice(deliveryTypeRequest.getPrice());
        deliveryTypeRepository.save(foundDeliveryType);
        return deliveryTypeMapper.mapToDto(foundDeliveryType);
    }

    public void deleteDeliveryType(Long deliveryID){
        deliveryTypeRepository.deleteById(deliveryID);
    }

    DeliveryType findDeliveryTypeById(Long deliveryTypeId) {
        return deliveryTypeRepository.findById(deliveryTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery type", "id", deliveryTypeId));
    }
}
