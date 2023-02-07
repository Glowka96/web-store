package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.ProducerMapper;
import com.example.porfolio.webstorespring.model.dto.products.ProducerDto;
import com.example.porfolio.webstorespring.model.entity.products.Producer;
import com.example.porfolio.webstorespring.repositories.CategoryRepository;
import com.example.porfolio.webstorespring.repositories.ProducerRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final ProducerRepository producerRepository;
    private final ProducerMapper producerMapper;

    public ProducerDto geProducerById(Long id) {
        Producer foundProducer = findProducerById(id);
        return producerMapper.mapToDto(foundProducer);
    }

    public ProducerDto save(ProducerDto producerDto) {
        Producer producer = producerMapper.mapToEntity(producerDto);
        producerRepository.save(producer);
        return producerMapper.mapToDto(producer);
    }

    public ProducerDto update(Long id, ProducerDto producerDto) {
        Producer foundProducer = findProducerById(id);

        Producer producer = producerMapper.mapToEntity(producerDto);
        setupProducer(foundProducer, producer);

        producerRepository.save(producer);
        return producerMapper.mapToDto(producer);
    }

    public void delete(ProducerDto producerDto) {
        Producer producer = producerMapper.mapToEntity(producerDto);
        producerRepository.delete(producer);
    }

    private Producer findProducerById(Long id) {
        return producerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producer", "id", id));
    }

    private void setupProducer(Producer foundProducer,
                               Producer updatedProducer) {
        updatedProducer.setId(foundProducer.getId());
        if (updatedProducer.getName() == null) {
            updatedProducer.setName(foundProducer.getName());
        }
        if (updatedProducer.getProducts() == null) {
            updatedProducer.setProducts(foundProducer.getProducts());
        }
    }
}
