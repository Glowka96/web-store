package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProducerMapper;
import com.example.portfolio.webstorespring.models.dtos.products.requests.ProducerRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.ProducerResponse;
import com.example.portfolio.webstorespring.models.entities.products.Producer;
import com.example.portfolio.webstorespring.repositories.products.ProducerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerService {

    private final ProducerRepository producerRepository;

    public ProducerResponse getById(Long id) {
        log.info("Fetching producer for ID: {}", id);
        return ProducerMapper.mapToResponse(findById(id));
    }

    public List<ProducerResponse> getAll() {
        log.info("Fetching all producer.");
        return ProducerMapper.mapToResponse(producerRepository.findAll());
    }

    public ProducerResponse save(ProducerRequest request) {
        log.info("Saving producer from request: {}", request);
        Producer producer = ProducerMapper.mapToEntity(request);
        producerRepository.save(producer);
        log.info("Saved producer.");
        return ProducerMapper.mapToResponse(producer);
    }

    @Transactional
    public ProducerResponse update(Long id, ProducerRequest request) {
        log.info("Updating producer for ID: {}, form request: {}", id, request);
        Producer foundProducer = findById(id);
        foundProducer.setName(request.name());
        producerRepository.save(foundProducer);
        log.info("Updated producer.");
        return ProducerMapper.mapToResponse(foundProducer);
    }

    public void deleteById(Long id) {
        log.info("Deleting producer for ID: {}", id);
        producerRepository.deleteById(id);
    }

    protected Producer findById(Long id) {
        return producerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producer", "id", id));
    }
}
