package com.example.portfolio.webstorespring.buildhelpers;

import com.example.portfolio.webstorespring.model.dto.products.request.ProducerRequest;
import com.example.portfolio.webstorespring.model.entity.products.Producer;

public class ProducerBuilderHelper {

    public static Producer createProducer() {
        return Producer.builder()
                .id(1L)
                .name("Test")
                .build();
    }

    public static ProducerRequest createProducerRequest() {
        return ProducerRequest.builder()
                .name("Test")
                .build();
    }

    public static ProducerRequest createProducerRequest(String name) {
        return ProducerRequest.builder()
                .name(name)
                .build();
    }
}