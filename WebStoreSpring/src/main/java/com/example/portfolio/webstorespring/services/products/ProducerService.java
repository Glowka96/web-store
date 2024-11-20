package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProducerMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.ProducerRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProducerResponse;
import com.example.portfolio.webstorespring.model.entity.products.Producer;
import com.example.portfolio.webstorespring.repositories.products.ProducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final ProducerRepository producerRepository;

    public ProducerResponse getById(Long id) {
        return ProducerMapper.mapToDto(findById(id));
    }

    public List<ProducerResponse> getAll() {
        return ProducerMapper.mapToDto(producerRepository.findAll());
    }

    public ProducerResponse save(ProducerRequest producerRequest) {
        Producer producer = ProducerMapper.mapToEntity(producerRequest);
        producerRepository.save(producer);
        return ProducerMapper.mapToDto(producer);
    }

    @Transactional
    public ProducerResponse update(Long id, ProducerRequest producerRequest) {
        Producer foundProducer = findById(id);
        foundProducer.setName(producerRequest.name());
        producerRepository.save(foundProducer);
        return ProducerMapper.mapToDto(foundProducer);
    }

    public void deleteById(Long id) {
        producerRepository.deleteById(id);
    }

    protected Producer findById(Long id) {
        return producerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producer", "id", id));
    }
}
