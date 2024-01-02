package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProducerMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.ProducerRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProducerResponse;
import com.example.portfolio.webstorespring.model.entity.products.Producer;
import com.example.portfolio.webstorespring.repositories.products.ProducerRepository;
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

    public ProducerResponse saveProducer(ProducerRequest producerRequest) {
        Producer producer = producerMapper.mapToEntity(producerRequest);
        producerRepository.save(producer);
        return producerMapper.mapToDto(producer);
    }

    public ProducerResponse updateProducer(Long id, ProducerRequest producerRequest) {
        Producer foundProducer = findProducerById(id);

        Producer producer = producerMapper.mapToEntity(producerRequest);
        setupUpdateProducer(foundProducer, producer);

        producerRepository.save(producer);
        return producerMapper.mapToDto(producer);
    }

    public void deleteProducerById(Long id) {
        Producer foundProducer = findProducerById(id);
        producerRepository.delete(foundProducer);
    }

    private Producer findProducerById(Long id) {
        return producerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producer", "id", id));
    }

    private void setupUpdateProducer(Producer foundProducer,
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
