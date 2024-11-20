package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.ProducerRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProducerResponse;
import com.example.portfolio.webstorespring.model.entity.products.Producer;

public class ProducerBuilderHelper {

    public static Producer createProducer() {
        return Producer.builder()
                .id(1L)
                .name("Test")
                .build();
    }

    public static Producer createProducerWithoutId(String name) {
        return Producer.builder()
                .name(name)
                .build();
    }

    public static ProducerRequest createProducerRequest() {
        return createProducerRequest("Test");
    }

    public static ProducerRequest createProducerRequest(String name) {
        return new ProducerRequest(name);
    }

    public static ProducerResponse createProducerResponse() {
        return new ProducerResponse(1L, "Test");
    }
}
