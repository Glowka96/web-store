package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.products.request.ProducerRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProducerResponse;
import com.example.portfolio.webstorespring.model.entity.products.Producer;

import java.util.List;


public interface ProducerMapper {

    static List<ProducerResponse> mapToDto(List<Producer> producers) {
        return producers.stream()
                .map(ProducerMapper::mapToDto)
                .toList();
    }

    static ProducerResponse mapToDto(Producer producer) {
        return new ProducerResponse(
                producer.getId(),
                producer.getName()
        );
    }

    static Producer mapToEntity(ProducerRequest producerRequest) {
        return Producer.builder()
                .name(producerRequest.name())
                .build();
    }
}
