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

    public List<DeliveryTypeResponse> getAll() {
        return deliveryTypeMapper.mapToDto(deliveryTypeRepository.findAll());
    }

    public DeliveryTypeResponse save(DeliveryTypeRequest deliveryTypeRequest){
        DeliveryType deliveryType = deliveryTypeMapper.mapToEntity(deliveryTypeRequest);

        deliveryTypeRepository.save(deliveryType);
        return deliveryTypeMapper.mapToDto(deliveryType);
    }

    public DeliveryTypeResponse update(Long id, DeliveryTypeRequest deliveryTypeRequest) {
        DeliveryType foundDeliveryType = findById(id);

        foundDeliveryType.setName(deliveryTypeRequest.getName());
        foundDeliveryType.setPrice(deliveryTypeRequest.getPrice());
        deliveryTypeRepository.save(foundDeliveryType);
        return deliveryTypeMapper.mapToDto(foundDeliveryType);
    }

    public void deleteById(Long id){
        deliveryTypeRepository.deleteById(id);
    }

    DeliveryType findById(Long id) {
        return deliveryTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery type", "id", id));
    }
}
