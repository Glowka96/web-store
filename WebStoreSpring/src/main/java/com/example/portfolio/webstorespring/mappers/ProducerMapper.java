package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.products.ProducerRequest;
import com.example.portfolio.webstorespring.model.dto.products.ProducerResponse;
import com.example.portfolio.webstorespring.model.entity.products.Producer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface ProducerMapper {
    ProducerResponse mapToDto(Producer producer);

    List<ProducerResponse> mapToDto(List<Producer> producers);

    @Mapping(target = "products", ignore = true)
    @Mapping(target = "id", ignore = true)
    Producer mapToEntity(ProducerRequest producerRequest);
}
