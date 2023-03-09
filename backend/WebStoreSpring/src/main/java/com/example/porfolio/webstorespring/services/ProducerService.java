package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.ProducerMapper;
import com.example.porfolio.webstorespring.model.dto.products.ProducerDto;
import com.example.porfolio.webstorespring.model.entity.products.Producer;
import com.example.porfolio.webstorespring.repositories.ProducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final ProducerRepository producerRepository;
    private final ProducerMapper producerMapper;

    public ProducerDto getProducerById(Long id) {
        Producer foundProducer = findProducerById(id);
        return producerMapper.mapToDto(foundProducer);
    }

    public List<ProducerDto> getAllProducer() {
        return producerMapper.mapToDto(producerRepository.findAll());
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

    public void deleteById(Long id) {
        Producer foundProducer = findProducerById(id);
        producerRepository.delete(foundProducer);
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
