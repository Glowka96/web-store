package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.models.dtos.products.requests.ProducerRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.ProducerResponse;
import com.example.portfolio.webstorespring.models.entities.products.Producer;

import java.util.List;


public interface ProducerMapper {

    static List<ProducerResponse> mapToResponse(List<Producer> producers) {
        return producers.stream()
                .map(ProducerMapper::mapToResponse)
                .toList();
    }

    static ProducerResponse mapToResponse(Producer producer) {
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
