package com.example.porfolio.webstorespring.services.products;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.ProducerMapper;
import com.example.porfolio.webstorespring.model.dto.products.ProducerRequest;
import com.example.porfolio.webstorespring.model.dto.products.ProducerResponse;
import com.example.porfolio.webstorespring.model.entity.products.Producer;
import com.example.porfolio.webstorespring.repositories.products.ProducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final ProducerRepository producerRepository;
    private final ProducerMapper producerMapper;

    public ProducerResponse getProducerById(Long id) {
        Producer foundProducer = findProducerById(id);
        return producerMapper.mapToDto(foundProducer);
    }

    public List<ProducerResponse> getAllProducer() {
        return producerMapper.mapToDto(producerRepository.findAll());
    }

    public ProducerResponse save(ProducerRequest producerRequest) {
        Producer producer = producerMapper.mapToEntity(producerRequest);
        producerRepository.save(producer);
        return producerMapper.mapToDto(producer);
    }

    public ProducerResponse update(Long id, ProducerRequest producerRequest) {
        Producer foundProducer = findProducerById(id);

        Producer producer = producerMapper.mapToEntity(producerRequest);
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
