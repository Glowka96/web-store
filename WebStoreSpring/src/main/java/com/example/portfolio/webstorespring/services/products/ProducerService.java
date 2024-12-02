package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProducerMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.ProducerRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProducerResponse;
import com.example.portfolio.webstorespring.model.entity.products.Producer;
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
        return ProducerMapper.mapToDto(findById(id));
    }

    public List<ProducerResponse> getAll() {
        log.info("Fetching all producer.");
        return ProducerMapper.mapToDto(producerRepository.findAll());
    }

    public ProducerResponse save(ProducerRequest producerRequest) {
        log.info("Saving producer from request: {}", producerRequest);
        Producer producer = ProducerMapper.mapToEntity(producerRequest);
        producerRepository.save(producer);
        log.info("Saved producer.");
        return ProducerMapper.mapToDto(producer);
    }

    @Transactional
    public ProducerResponse update(Long id, ProducerRequest producerRequest) {
        log.info("Updating producer for ID: {}, form request: {}", id, producerRequest);
        Producer foundProducer = findById(id);
        foundProducer.setName(producerRequest.name());
        producerRepository.save(foundProducer);
        log.info("Updated producer.");
        return ProducerMapper.mapToDto(foundProducer);
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
