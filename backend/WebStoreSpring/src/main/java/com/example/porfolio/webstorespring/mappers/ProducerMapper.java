package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.products.ProducerDto;
import com.example.porfolio.webstorespring.model.entity.products.Producer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface ProducerMapper {
    @Mapping(target = "productsDto", source = "products")
    ProducerDto mapToDto(Producer producer);

    @Mapping(target = "productsDto", source = "products")
    List<ProducerDto> mapToDto(List<Producer> producers);

    @Mapping(target = "products", source = "productsDto")
    Producer mapToEntity(ProducerDto producerDto);
}
