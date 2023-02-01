package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.products.ProducerDto;
import com.example.porfolio.webstorespring.model.entity.products.Producer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {
                ProductMapper.class
        }
)
public interface ProducerMapper {
    @Mapping(target = "productsDto", ignore = true)
    ProducerDto mapToDto(Producer producer);

    @Mapping(target = "products", ignore = true)
    Producer mapToEntity(ProducerDto producerDto);
}
